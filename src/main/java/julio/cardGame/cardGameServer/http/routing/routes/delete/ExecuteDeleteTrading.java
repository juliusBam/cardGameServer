package julio.cardGame.cardGameServer.http.routing.routes.delete;

import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.tradingServices.DeleteTradingService;

public class ExecuteDeleteTrading extends ServiceableRoute implements Routeable {

    public ExecuteDeleteTrading() {
        this.service = this.initiateCardGameService();
    }

    @Override
    protected CardGameService initiateCardGameService() {
        return new DeleteTradingService();
    }

    @Override
    public Response process(RequestContext requestContext) {

        return this.executeAuthenticatedService(requestContext, null);

    }

}
