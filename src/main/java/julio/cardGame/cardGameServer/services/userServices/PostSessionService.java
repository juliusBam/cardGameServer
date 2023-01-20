package julio.cardGame.cardGameServer.services.userServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.models.UserLoginDataModel;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class PostSessionService implements CardGameService {
    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException, NoSuchAlgorithmException {

        UserLoginDataModel userModel = new ObjectMapper()
                .readValue(requestContext.getBody(), UserLoginDataModel.class);

        String token = new UserRepo().loginUser(userModel);

        if (token == null)
            return new Response(DefaultMessages.ERR_NO_FOUND_USER.getMessage(), HttpStatus.BAD_REQUEST);

        return new Response(token, HttpStatus.OK);

    }
}
