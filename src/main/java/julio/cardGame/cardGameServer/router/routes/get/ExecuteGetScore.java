package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.ScoreModel;
import julio.cardGame.common.models.StatsModel;

import java.util.ArrayList;
import java.util.List;

public class ExecuteGetScore implements Route {
    @Override
    public Response process(RequestContext requestContext) {

        //todo db logic

        List<ScoreModel> stats = new ArrayList<>();

        stats.add(new ScoreModel(50, 50, 50.0, 800,"user1"));
        stats.add(new ScoreModel(30,30,50.0,800, "user2"));

        if (stats.size() == 0) {
            return new Response(DefaultMessages.SCORE_NO_RESULTS.getMessage(), HttpStatus.OK);
        }

        try {
            String body = new ObjectMapper()
                    .writeValueAsString(stats);

            return new Response(body, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_SCOREBOARD.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
