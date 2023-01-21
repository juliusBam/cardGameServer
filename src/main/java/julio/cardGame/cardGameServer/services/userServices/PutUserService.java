package julio.cardGame.cardGameServer.services.userServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.models.UserAdditionalDataModel;
import julio.cardGame.cardGameServer.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.sql.SQLException;

public class PutUserService implements CardGameService {
    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());


        UserAdditionalDataModel userModel = new ObjectMapper()
                .readValue(requestContext.getBody(), UserAdditionalDataModel.class);


        if (!requestedUser.equals(requestedUser))
            return new Response(DefaultMessages.ERR_MISMATCHING_USERS.getMessage(), HttpStatus.BAD_REQUEST);

        //this.updateUser(requestedUser, userModel);
        new UserRepo().updateUser(userModel, requestedUser);

        return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

    }
}
