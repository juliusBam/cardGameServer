package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.CardRepo;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.application.dbLogic.models.CardDbModel;

import java.sql.SQLException;
import java.util.List;

public class ExecuteGetCard extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            List<CardDbModel> cards = new CardRepo().fetchUserCards(auth.userName);

            if (cards.size() == 0)
                return new Response(DefaultMessages.USER_NO_CARDS.getMessage(), HttpStatus.OK);

            String body = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(cards);

            return new Response(body, HttpStatus.OK, true);


        } catch (SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_CARDS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


        /*String sql = """
                SELECT * 
                    FROM cards 
                        WHERE "ownerID"=(SELECT "userID" 
                                            FROM users 
                                                WHERE "userName"=?);
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)){


            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<CardDbModel> cards = new ArrayList<>();

            while (resultSet.next()) {
                cards.add(
                  new CardDbModel(
                          resultSet.getObject(1, UUID.class),
                          resultSet.getObject(2, UUID.class),
                          resultSet.getObject(3, UUID.class),
                          resultSet.getString(4),
                          resultSet.getDouble(5),
                          resultSet.getString(6),
                          resultSet.getString(7),
                          resultSet.getString(8)
                  )
                );
            }

            if (cards.size() == 0)
                return new Response(DefaultMessages.USER_NO_CARDS.getMessage(), HttpStatus.OK);

            String body = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(cards);

            return new Response(body, HttpStatus.OK);

        } catch (SQLException | JsonProcessingException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }*/

    }
}
