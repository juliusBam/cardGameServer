package julio.cardGame.cardGameServer.services.userServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.models.StatsModel;
import julio.cardGame.cardGameServer.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.sql.SQLException;

public class GetStatsService implements CardGameService {


    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        UserRepo userRepo = new UserRepo();

        StatsModel stats = userRepo.fetchUserStats(authorizationWrapper.userName);

        if (stats == null)
            return new Response(DefaultMessages.ERR_NO_STATS.getMessage(), HttpStatus.OK);

        String body = new ObjectMapper()
                .writeValueAsString(stats);

        return new Response(body, HttpStatus.OK, true);

    }
}
