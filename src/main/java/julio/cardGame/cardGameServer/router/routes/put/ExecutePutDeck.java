package julio.cardGame.cardGameServer.router.routes.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.dbLogic.db.DbConnection;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.CardRepo;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.Constants;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;

import javax.naming.AuthenticationException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ExecutePutDeck extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            List<UUID> cardIds = new ObjectMapper()
                    .readValue(requestContext.getBody(), new TypeReference<List<UUID>>() {
                    });

            //only 4 cards allowed
            if (cardIds.size() != Constants.DECK_SIZE)
                return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);

            try (Connection dbConn = DbConnection.getInstance().connect()) {

                try {

                    //once the cardids are parsed we can execute the sqls
                    UserRepo userRepo = new UserRepo();

                    boolean hasDeck = userRepo.checkIfDeck(dbConn, auth.userName);

                    //user already has a deck
                    if (hasDeck) {
                        //dbConn.close();
                        return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
                    }

                    //we check if all the cards belong to the user
                    int ownedCards = userRepo.checkCardsOwnership(dbConn, cardIds, auth.userName);

                    if (ownedCards != Constants.DECK_SIZE) {
                        //dbConn.close();
                        return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
                    }

                    UUID newDeckID = UUID.randomUUID();

                    dbConn.setAutoCommit(false);

                    userRepo.addDeckID(dbConn, newDeckID, auth.userName);

                    CardRepo cardRepo = new CardRepo();

                    cardRepo.moveCardsToDeck(dbConn, newDeckID, cardIds);

                    dbConn.commit();

                } catch (SQLException e) {

                    //if we started the transaction we rollback
                    if (!dbConn.getAutoCommit()) {
                        dbConn.rollback();
                    }

                    //dbConn.close();
                    return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

                } catch (AuthenticationException e) {

                    //dbConn.close();
                    return new Response(e.getMessage(), HttpStatus.UNAUTHORIZED);

                }

            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_DECK.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

    }

    /*private void moveCardsToDeck(Connection dbConn, UUID newDeckID, List<UUID> cardIds) throws SQLException {

        String sql = """
               UPDATE cards
                    SET "deckID"=?
                            WHERE "cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=?;
                """;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)){

            preparedStatement.setObject(1, DataTransformation.prepareUUID(newDeckID));

            for (int i = 0; i < Constants.DECK_SIZE; i++) {
                preparedStatement.setObject(i + 2, DataTransformation.prepareUUID(cardIds.get(i)));
            }

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }*/

    /*private void addDeckID(Connection dbConn, UUID newDeckID, String userName) throws SQLException {

        String sql = """
                    UPDATE users
                        SET "deckID"=?
                            WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(newDeckID));
            preparedStatement.setString(2, userName);

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }*/

    /*private boolean checkIfDeck(Connection dbConn, String userName) throws SQLException, AuthenticationException {

        String sql = """
                    SELECT "deckID"
                        FROM users
                            WHERE "userName"=?;
                """;

        boolean hasDeck = false;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                UUID deckID = resultSet.getObject(1, UUID.class);

                if (deckID != null)
                    hasDeck = true;

            } else {
                preparedStatement.close();
                throw new AuthenticationException("User does not exist");
            }

        } catch (SQLException e) {
            throw e;
        }

        return hasDeck;

    }*/

    /*private int checkCardsOwnership(Connection dbConn, List<UUID> cardIds, String userName) throws SQLException {

        String sql = """
                    SELECT distinct COUNT("cardID")
                        FROM cards
                            WHERE ("cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=?) 
                                    AND "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?)
                """;

        int ownedCards = 0;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {

            for (int i = 0; i < Constants.DECK_SIZE; ++i) {
                preparedStatement.setObject(i+1, DataTransformation.prepareUUID(cardIds.get(i)));
            }

            preparedStatement.setString(Constants.DECK_SIZE + 1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ownedCards = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw e;
        }

        return ownedCards;

    }*/

}
