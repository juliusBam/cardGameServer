package julio.cardGame.cardGameServer.http.routing.routes.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.routes.ServiceableRoute;
import julio.cardGame.cardGameServer.services.userServices.ActivateUserService;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ExecutePutActivate extends ServiceableRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) throws JsonProcessingException, NoSuchAlgorithmException, SQLException {

        return this.executeAdminService(requestContext, DefaultMessages.ERR_JSON_PARSE_USER.getMessage());

    }

    @Override
    protected CardGameService initiateCardGameService() {
        return new ActivateUserService();
    }
}
