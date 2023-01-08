package julio.cardGame.cardGameServer.router.routes.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExecutePutDeck implements Route {
    @Override
    public Response process(RequestContext requestContext) {


        try {

            List<UUID> cardIds = new ObjectMapper()
                    .readValue(requestContext.getBody(), new TypeReference<List<UUID>>() {
                    });

            //todo insert deck into db --> check if cards belong to the user

            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_DECK.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }
}
