package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.cardGameServer.application.serverLogic.models.CardDeckModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExecuteGetDeck extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

        if (auth.response != null)
            return auth.response;

            try (Connection dbConn = DbConnection.getInstance().connect()) {

                    List<CardDeckModel> deckObj = this.fetchDeckCards(dbConn, auth.userName);

                    if (deckObj.size() == 0)
                        return new Response(DefaultMessages.USER_NO_DECK.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

                    String body = "";

                    if (requestContext.fetchParameter(RequestParameters.FORMAT.getParamValue()) != null) {

                        for (CardDeckModel card : deckObj) {
                            body += card.cardName + ", ";
                            body += card.cardType + ", ";
                            body += card.card_damage + ", ";
                            body += card.cardElement + ", ";
                            body += card.monsterRace + "\n";
                        }

                    } else {

                        body = new ObjectMapper()
                                .writerWithDefaultPrettyPrinter()
                                .writeValueAsString(deckObj);

                    }

                return new Response(body, HttpStatus.OK);



                    //return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK)

                //return new Response(body, HttpStatus.OK);

            } catch (SQLException e) {

                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            } catch (JsonProcessingException e) {

                return new Response(DefaultMessages.ERR_JSON_PARSE_DECK.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            }

    }

    private List<CardDeckModel> fetchDeckCards(Connection dbConn, String userName) throws SQLException {

        //todo fetch deck from db

        String sql = """
                        SELECT "cardName", "card_damage", "cardElement", "cardType", "monsterRace"
                            FROM cards
                            WHERE "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?) 
                                AND "deckID"=(SELECT "deckID" FROM users WHERE "userName"=?);
                """;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<CardDeckModel> deck = new ArrayList<>();

            while (resultSet.next()) {

                deck.add(
                    new CardDeckModel(
                        resultSet.getString(1),
                            resultSet.getDouble(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    )
                );

            }

            return deck;


        } catch (SQLException e) {
            throw e;
        }
        /*List<CardRequestModel> cards = new ArrayList<>();

        cards.add(new CardRequestModel());


        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(cards);

        if (cards.size() == 0)
            return null;*/


    }

}
