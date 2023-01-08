package julio.cardGame.cardGameServer.router.routes.post;

import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.HttpStatus;

public class ExecutePostTransactionPackage implements Route {

    @Override
    public Response process(RequestContext requestContext) {

        //todo add db logic --> check if user has money left and if there are packages

        return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

    }
}
