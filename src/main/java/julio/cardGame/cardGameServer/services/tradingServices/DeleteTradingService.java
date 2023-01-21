package julio.cardGame.cardGameServer.services.tradingServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.repositories.TradeRepo;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.RequestParameters;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.sql.SQLException;
import java.util.UUID;

public class DeleteTradingService implements CardGameService {


    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        String stringTradeId = requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue());

        if (stringTradeId == null)
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);

        UUID tradeId = UUID.fromString(stringTradeId);

        if (AuthenticationController.requireAdmin(requestContext.getHeaders()).isAdmin) {

            TradeRepo tradeRepo = new TradeRepo();

            tradeRepo.deleteTradeAdmin(tradeId);

        } else {

            String userName = authorizationWrapper.userName;

            if (userName == null)
                return new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED);

            TradeRepo tradeRepo = new TradeRepo();

            tradeRepo.deleteTradeNormalUser(tradeId, userName);

        }

        return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

    }
}
