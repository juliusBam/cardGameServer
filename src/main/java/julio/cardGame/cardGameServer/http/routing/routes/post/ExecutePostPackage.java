package julio.cardGame.cardGameServer.http.routing.routes.post;

import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.cardsServices.PostPackageService;

public class ExecutePostPackage extends ServiceableRoute implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        return this.executeAdminService(requestContext, null);

    }

    @Override
    protected CardGameService initiateCardGameService() {
        return new PostPackageService();
    }
}
