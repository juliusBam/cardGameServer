package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.models.CardRequestModel;

import java.util.ArrayList;
import java.util.List;

public class ExecuteGetCard implements Route {
    @Override
    public Response process(RequestContext requestContext) {


        //todo fetch cards from db
        List<CardRequestModel> cards = new ArrayList<>();

        if (cards.size() == 0) {
            return new Response(DefaultMessages.USER_NO_CARDS.getMessage(), HttpStatus.OK);
        } else {

            try {
                String body = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(cards);


                return new Response(body, HttpStatus.OK);
            } catch (JsonProcessingException e) {

                return new Response(DefaultMessages.ERR_JSON_PARSE_CARDS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            }

        }


    }
}
