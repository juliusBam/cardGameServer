package julio.cardGame.cardGameServer.http.routing.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.database.repositories.TradeRepo;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.battle.cards.CardTypes;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestParameters;
import julio.cardGame.cardGameServer.database.models.TradeModel;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.CardGameService;
import julio.cardGame.cardGameServer.services.PostTradingService;

import java.sql.*;
import java.util.UUID;

public class ExecutePostTrading extends ServiceableRoute implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        return this.executeAuthenticatedService(requestContext, DefaultMessages.ERR_JSON_PARSE_TRADE.getMessage());

    }

    @Override
    protected CardGameService initiateCardGameService() {
        return new PostTradingService();
    }
}
