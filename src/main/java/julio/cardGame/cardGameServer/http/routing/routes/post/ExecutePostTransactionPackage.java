package julio.cardGame.cardGameServer.http.routing.routes.post;

import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.database.repositories.PackageRepo;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.headers.HeadersValidator;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.database.models.PackageModel;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.PostPackageService;
import julio.cardGame.cardGameServer.services.PostTransactionPackagesService;

import java.sql.*;

public class ExecutePostTransactionPackage extends ServiceableRoute implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        return this.executeAuthenticatedService(requestContext, null);

    }

    @Override
    protected CardGameService initiateCardGameService() {
        return new PostTransactionPackagesService();
    }
}
