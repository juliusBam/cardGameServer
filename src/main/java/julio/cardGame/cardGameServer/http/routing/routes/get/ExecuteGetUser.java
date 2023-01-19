package julio.cardGame.cardGameServer.http.routing.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;

import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestParameters;
import julio.cardGame.cardGameServer.database.models.CompleteUserModel;

import java.sql.SQLException;

public class ExecuteGetUser implements Routeable {

    private final UserRepo userRepo;

    public ExecuteGetUser() {
        this.userRepo = new UserRepo();
    }

    @Override
    public Response process(RequestContext requestContext) {

        try {

            //extract the selected user
            String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

            if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank())
                return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            //check if admin or requestedUser == logged user
            AuthorizationWrapper authorizationWrapper = AuthenticationController.canAccessUserData(requestContext.getHeaders(), requestedUser);

            if (authorizationWrapper.response != null)
                return authorizationWrapper.response;

            CompleteUserModel userData = this.userRepo.getUser(requestedUser);

            String body = new ObjectMapper()
                    .writeValueAsString(userData);

            return new Response(body, HttpStatus.OK, true);

        } catch (SQLException e) {

            return new Response(e);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), e);

        }

    }
}
