package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.StatsModel;

public class ExecuteGetStats implements Route {
    @Override
    public Response process(RequestContext requestContext) {

        //todo db logic

        StatsModel statsModel = new StatsModel(5,5,50,800);

        try {

            String body = new ObjectMapper()
                    .writeValueAsString(statsModel);

            return new Response(body, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_STATS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
