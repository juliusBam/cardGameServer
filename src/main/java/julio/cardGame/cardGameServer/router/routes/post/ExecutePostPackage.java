package julio.cardGame.cardGameServer.router.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DataTransformation;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.HeadersValidator;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.CardDbModel;
import julio.cardGame.common.models.CardRequestModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExecutePostPackage implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        String authToken = HeadersValidator.validateToken(requestContext.getHeaders());

        if (authToken == null || !HeadersValidator.checkAdmin(authToken))
            return new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED);

        try {

            List<CardRequestModel> newPackage = new ObjectMapper()
                    .readValue(requestContext.getBody(), new TypeReference<List<CardRequestModel>>() {
                    });

            List<CardDbModel> cardsToInsert = new ArrayList<>();
            List<UUID> cardIDs = new ArrayList<>();

            //converts the request object to a desired format
            for (CardRequestModel card : newPackage) {
                cardsToInsert.add(new CardDbModel(card));
                cardIDs.add(card.Id);
            }

            String sqlInsertCards = """
                INSERT INTO
                    cards("cardID", "cardName", "card_damage", "cardElement", "cardType", "monsterRace")
                VALUES
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?);
                """;

            String sqlInsertPackage = """
                INSERT INTO public.packages
                    VALUES (?,?,?,?,?,?);
                """;

            //dbConnection has to be in a separeted try catch, so that the the rollback can be executed at line 74
            try (Connection dbConn = DbConnection.getInstance().connect()) {

                try (PreparedStatement statementInsertCards = dbConn.prepareStatement(sqlInsertCards);
                     PreparedStatement statementInsertPackage = dbConn.prepareStatement(sqlInsertPackage)
                ){

                    //begin transaction, since we have to execute 2 sqls depending upon each other
                    dbConn.setAutoCommit(false);

                    addCards(statementInsertCards, cardsToInsert);
                    createPackage(statementInsertPackage, cardIDs);

                    dbConn.commit();

                } catch (SQLException e) {
                    try {
                        dbConn.rollback();

                    } catch (SQLException ex) {

                        return new Response(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

                    }

                    return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

                }

                return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
            return new Response(DefaultMessages.ERR_JSON_PARSE_PACKAGE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void addCards(PreparedStatement preparedStatement, List<CardDbModel> cards) throws SQLException {

            for (int i = 0; i < 5; ++i) {

                preparedStatement.setObject(1 + (i * 6), DataTransformation.prepareUUID(cards.get(i).cardID));
                preparedStatement.setString(2 + (i * 6), cards.get(i).cardName);
                preparedStatement.setDouble(3 + (i * 6), cards.get(i).card_damage);
                preparedStatement.setObject(4 + (i * 6), cards.get(i).cardElement, Types.OTHER);
                preparedStatement.setObject(5 + (i * 6), cards.get(i).cardType, Types.OTHER);
                preparedStatement.setObject(6 + (i * 6), cards.get(i).monsterRace, Types.OTHER);

            }

            preparedStatement.execute();
            preparedStatement.close();

    }

    private void createPackage(PreparedStatement preparedStatement, List<UUID> cardsIDs) throws SQLException {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(UUID.randomUUID()));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(cardsIDs.get(0)));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(cardsIDs.get(1)));
            preparedStatement.setObject(4, DataTransformation.prepareUUID(cardsIDs.get(2)));
            preparedStatement.setObject(5, DataTransformation.prepareUUID(cardsIDs.get(3)));
            preparedStatement.setObject(6, DataTransformation.prepareUUID(cardsIDs.get(4)));

            preparedStatement.execute();
            preparedStatement.close();

    }
}
