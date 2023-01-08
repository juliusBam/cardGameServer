package julio.cardGame.cardGameServer.router.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.common.models.TradeModel;

import java.util.UUID;

public class ExecutePostTrading implements Route {
    @Override
    public Response process(RequestContext requestContext) {


        if (requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue()) == null) {

            return this.executePostNewDeal(requestContext);

        } else {

            return this.executeAcceptDeal(requestContext);

        }

    }

    public Response executePostNewDeal(RequestContext requestContext) {
        try {

            TradeModel tradeModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), TradeModel.class);

            //todo db logic

            return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_TRADE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public Response executeAcceptDeal(RequestContext requestContext) {

        try {

            UUID tradeUUID = UUID.fromString(requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue()));

            UUID cardUUID = UUID.fromString(requestContext.getBody().replace("\"",""));

            //todo db logic --> check if proposed card

            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            return new Response(DefaultMessages.ERR_INVALID_TRADE_UUID.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }
}
