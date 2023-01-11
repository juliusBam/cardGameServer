package julio.cardGame.cardGameServer.router.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DataTransformation;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.CardTypes;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.cardGameServer.application.serverLogic.models.TradeModel;

import java.sql.*;
import java.util.UUID;

public class ExecutePostTrading extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

        if (auth.response != null)
            return auth.response;


        if (requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue()) == null) {

            return this.executePostNewDeal(requestContext, auth.userName);

        } else {

            return this.executeAcceptDeal(requestContext, auth.userName);

        }

    }

    //todo check if card belongs to user
    public Response executePostNewDeal(RequestContext requestContext, String userName) {
        try {

            TradeModel tradeModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), TradeModel.class);

            CardTypes requiredType = DataTransformation.convertIntoCardType(tradeModel.type);

            if (requiredType == null)
                throw new IllegalArgumentException();

            //todo db logic
            try (Connection dbConnection = DbConnection.getInstance().connect()) {

                //check if card belongs to user
                if (!this.checkIfOwnsCard(dbConnection, userName, tradeModel.cardToTrade))
                    return new Response(DefaultMessages.ERR_CARD_NOT_OWNED.getMessage(), HttpStatus.BAD_REQUEST);

                //execute insert trade deal
                this.insertDeal(dbConnection, userName, tradeModel, requiredType);

                return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (JsonProcessingException | IllegalArgumentException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_TRADE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    private void insertDeal(Connection dbConnection, String userName, TradeModel tradeModel, CardTypes requiredType) throws SQLException {

        String sql = """
                            INSERT INTO trades
                                VALUES (?,?,?, (SELECT "userID" FROM users where "userName"=?), ?);
                        """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeModel.id));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(tradeModel.cardToTrade));
            preparedStatement.setInt(3, tradeModel.minimumDamage);
            preparedStatement.setString(4, userName);
            preparedStatement.setObject(5, requiredType.getCardType(), Types.OTHER);

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }


    }

    public Response executeAcceptDeal(RequestContext requestContext, String userName) {

        try {

            UUID tradeUUID = UUID.fromString(requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue()));

            UUID cardUUID = UUID.fromString(requestContext.getBody().replace("\"",""));

            //todo db logic
            try (Connection dbConnection = DbConnection.getInstance().connect()) {

                //first check if card belongs to user
                if (!this.checkIfOwnsCard(dbConnection, userName, cardUUID)) {
                    return new Response(DefaultMessages.ERR_CARD_NOT_OWNED.getMessage(), HttpStatus.BAD_REQUEST);
                }

                //check if the card fulfills the trade criteria
                if (!this.checkIfFulfills(dbConnection, cardUUID, tradeUUID)) {
                    return new Response(DefaultMessages.ERR_CARD_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST);
                }

                //check if self trading
                if (this.checkIfSelfTrade(dbConnection, tradeUUID, userName)) {
                    return new Response(DefaultMessages.ERR_NO_SELF_TRADE.getMessage(), HttpStatus.BAD_REQUEST);
                }

                //start transaction
                dbConnection.setAutoCommit(false);

                try {

                    //card in trade --> logged user ID will become new owner
                    this.changeOwnershipCardInTrade(dbConnection, userName, tradeUUID);

                    //offered card --> user ID of the trade will become new owner
                    this.changeOwnershipOfferedCard(dbConnection, tradeUUID, cardUUID);

                    //if both succeeded delete trade deal
                    this.deleteTrade(dbConnection, tradeUUID);

                } catch (SQLException e) {
                    dbConnection.rollback();
                    return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

                dbConnection.commit();
                //needed?

                return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);



            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (IllegalArgumentException e) {

            return new Response(DefaultMessages.ERR_INVALID_TRADE_UUID.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    private void deleteTrade(Connection dbConnection, UUID tradeUUID) throws SQLException {

        String sql = """
                DELETE
                    FROM trades
                        WHERE "tradeID"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)){

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    private void changeOwnershipOfferedCard(Connection dbConnection, UUID tradeUUID, UUID cardUUID) throws SQLException {

        String sql = """
                    UPDATE cards
                        SET "deckID"=null, "ownerID"=(SELECT "userID" FROM trades WHERE "tradeID"=?)
                        WHERE "cardID"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(cardUUID));

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    private void changeOwnershipCardInTrade(Connection dbConnection, String userName, UUID tradeUUID) throws SQLException {

        String sql = """
                UPDATE cards
                    SET "deckID"=null, "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?)
                        WHERE "cardID"=(SELECT "offeredCardID" FROM trades WHERE "tradeID"=?);
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setObject(2, DataTransformation.prepareUUID(tradeUUID));

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    private boolean checkIfOwnsCard(Connection dbConnection, String userName, UUID cardUUID) throws SQLException {

        String sql = """
                    SELECT count("cardID") 
                        FROM cards 
                            WHERE "cardID"=? AND 
                                    "ownerID"=(SELECT "userID" from users WHERE "userName"=?);
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)){

            preparedStatement.setObject(1, DataTransformation.prepareUUID(cardUUID));
            preparedStatement.setString(2, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;

        } catch (SQLException e) {
            throw e;
        }

    }

    private boolean checkIfFulfills(Connection dbConnection, UUID offeredCard, UUID tradeUUID) throws SQLException {

        String sql = """
                        SELECT count("tradeID")
                            FROM trades
                                WHERE "tradeID"=?
                                    AND "requiredCardType"=(SELECT "cardType" FROM cards WHERE "cardID"=?)
                                    AND "minimumDamage"<(SELECT card_damage FROM cards WHERE "cardID"=?);
                    """;


        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(offeredCard));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(offeredCard));

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;

        } catch (Exception e) {
            throw e;
        }

    }

    private boolean checkIfSelfTrade(Connection dbConnection, UUID tradeUUID, String userName) throws SQLException {

        String sqlFindTradeOwner = """
                    SELECT count("tradeID")
                        FROM trades
                            WHERE "tradeID"=? AND "userID"=(SELECT "userID" FROM users WHERE "userName"=?);
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlFindTradeOwner)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setString(2, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            throw e;
        }

    }
}
