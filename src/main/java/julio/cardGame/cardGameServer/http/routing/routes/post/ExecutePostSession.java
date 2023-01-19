package julio.cardGame.cardGameServer.http.routing.routes.post;

import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.PostSessionService;


public class ExecutePostSession extends ServiceableRoute implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        return this.executeService(requestContext, DefaultMessages.ERR_JSON_PARSE_USER.getMessage());

    }

    @Override
    protected CardGameService initiateCardGameService() {
        return new PostSessionService();
    }
}

