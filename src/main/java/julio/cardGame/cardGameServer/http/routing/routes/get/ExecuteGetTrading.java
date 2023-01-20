package julio.cardGame.cardGameServer.http.routing.routes.get;

import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.tradingServices.GetTradingService;

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
