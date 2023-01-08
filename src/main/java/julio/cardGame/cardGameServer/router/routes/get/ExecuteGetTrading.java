package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.TradeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExecuteGetTrading implements Route {
    @Override
    public Response process(RequestContext requestContext) {

        //todo fetch from db

        List<TradeModel> trades = new ArrayList<>();

        trades.add(new TradeModel(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "typeCard1",
                10
        ));

        trades.add(new TradeModel(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "typeCard2",
                15
        ));

        if (trades.size() == 0)
            return new Response(DefaultMessages.NO_TRADES.getMessage(), HttpStatus.OK);

        try {

            String body = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(trades);

            return new Response(body, HttpStatus.OK);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_TRADE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }
}
