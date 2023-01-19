package julio.cardGame.cardGameServer.http.routing.routes.put;

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
import julio.cardGame.cardGameServer.database.models.UserAdditionalDataModel;

import java.sql.SQLException;

public class ExecutePutUser implements Routeable {

    private final UserRepo userRepo;

    public ExecutePutUser() {
        this.userRepo = new UserRepo();
    }

    @Override
    public Response process(RequestContext requestContext) {

        try {

            String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

            if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank())
                return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            AuthorizationWrapper authorizationWrapper = AuthenticationController.canAccessUserData(requestContext.getHeaders(), requestedUser);

            if (authorizationWrapper.response != null)
                return authorizationWrapper.response;

            UserAdditionalDataModel userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserAdditionalDataModel.class);


            if (!requestedUser.equals(requestedUser))
                return new Response(DefaultMessages.ERR_MISMATCHING_USERS.getMessage(), HttpStatus.BAD_REQUEST);

            //this.updateUser(requestedUser, userModel);
            userRepo.updateUser(userModel, requestedUser);

            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);


        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), e);

        } catch (SQLException e) {

            return new Response(e);

        }

    }

}
