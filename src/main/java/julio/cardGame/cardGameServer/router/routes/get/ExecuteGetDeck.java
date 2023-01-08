package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.common.models.CardModel;

import java.util.ArrayList;
import java.util.List;

public class ExecuteGetDeck implements Route {
    @Override
    public Response process(RequestContext requestContext) {

        //todo fetch deck from db
        List<CardModel> cards = new ArrayList<>();

        cards.add(new CardModel());

        if (cards.size() == 0)
            return new Response(DefaultMessages.USER_NO_DECK.getMessage(), HttpStatus.OK);

            try {
                String body = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(cards);

                if (requestContext.fetchParameter(RequestParameters.FORMAT.getParamValue()) != null) {

                    //todo return with format = plain
                    return new Response("Deck plain format", HttpStatus.OK);

                } else {

                    return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);
                }


                //return new Response(body, HttpStatus.OK);
            } catch (JsonProcessingException e) {

                return new Response(DefaultMessages.ERR_JSON_PARSE_DECK.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            }
    }
}
