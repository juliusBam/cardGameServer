package julio.cardGame.cardGameServer.services.userServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.models.ChangeUserStatusModel;
import julio.cardGame.cardGameServer.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class DeactivateUserService implements CardGameService {
    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException, NoSuchAlgorithmException {

        ChangeUserStatusModel requestedUser = new ObjectMapper()
                .readValue(requestContext.getBody(), ChangeUserStatusModel.class);

        if (requestedUser.userName == null || requestedUser.userName.isEmpty() || requestedUser.userName.isBlank())
            return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.BAD_REQUEST);

        UserRepo userRepo = new UserRepo();

        userRepo.changeUserStatus(requestedUser.userName, false);

        return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

    }

}
