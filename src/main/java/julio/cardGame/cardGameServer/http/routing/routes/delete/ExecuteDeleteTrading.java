package julio.cardGame.cardGameServer.http.routing.routes.delete;

import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.repositories.TradeRepo;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.communication.headers.HeadersValidator;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.DeleteTradingService;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

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
