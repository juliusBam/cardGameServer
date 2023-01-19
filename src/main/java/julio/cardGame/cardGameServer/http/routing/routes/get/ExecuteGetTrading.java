package julio.cardGame.cardGameServer.http.routing.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.models.TradeViewModel;
import julio.cardGame.cardGameServer.database.repositories.TradeRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.GetTradingService;

import java.sql.SQLException;
import java.util.List;

public class ExecuteGetTrading extends ServiceableRoute implements Routeable {

    @Override
    protected CardGameService initiateCardGameService() {
        return new GetTradingService();
    }

    @Override
    public Response process(RequestContext requestContext) {

        return this.executeAuthenticatedService(requestContext, null);

    }

}
