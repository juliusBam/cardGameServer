package julio.cardGame.cardGameServer.router.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.CardModel;

import java.util.List;

public class ExecutePostPackage implements Route {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            List<CardModel> newPackage = new ObjectMapper()
                    .readValue(requestContext.getBody(), new TypeReference<List<CardModel>>() {
                    });

            //todo db logic

            return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
            return new Response(DefaultMessages.ERR_JSON_PARSE_PACKAGE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
