package julio.cardGame.cardGameServer.http.routing.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.database.models.CardDbModel;

import java.sql.SQLException;
import java.util.List;

public class ExecuteGetCard extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = requireAuthToken(requestContext.getHeaders());

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

    }
}
