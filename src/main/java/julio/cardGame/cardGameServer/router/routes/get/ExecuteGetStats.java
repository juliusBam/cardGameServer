package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.cardGameServer.application.dbLogic.models.StatsModel;

import java.sql.SQLException;

public class ExecuteGetStats extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            StatsModel stats = new UserRepo().fetchUserStats(auth.userName);

            if (stats == null)
                return new Response(DefaultMessages.ERR_NO_STATS.getMessage(), HttpStatus.OK);

            String body = new ObjectMapper()
                    .writeValueAsString(stats);

            return new Response(body, HttpStatus.OK);

        } catch (SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_STATS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
