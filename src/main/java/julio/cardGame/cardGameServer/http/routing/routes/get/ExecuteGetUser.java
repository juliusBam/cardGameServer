package julio.cardGame.cardGameServer.http.routing.routes.get;

import julio.cardGame.cardGameServer.database.repositories.UserRepo;

import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.GetUserService;

public class ExecuteGetUser extends ServiceableRoute implements Routeable {

    @Override
    protected CardGameService initiateCardGameService() {
        return new GetUserService();
    }

    @Override
    public Response process(RequestContext requestContext) {

        return this.executeAccessProtectedDataService(requestContext, null);

    }
}
