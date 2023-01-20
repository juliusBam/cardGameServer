package julio.cardGame.cardGameServer.http.routing.routes.post;

import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.PostBattleService;
import julio.cardGame.cardGameServer.services.CardGameService;


public class ExecutePostBattle extends ServiceableRoute implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        return this.executeAuthenticatedService(requestContext, null);

    }


    @Override
    protected CardGameService initiateCardGameService() {
        return new PostBattleService();
    }

}
