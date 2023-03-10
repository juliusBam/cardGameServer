package julio.cardGame.cardGameServer.services.userServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.models.UserLoginDataModel;
import julio.cardGame.cardGameServer.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class PostUserService implements CardGameService {
    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException, NoSuchAlgorithmException {

        UserLoginDataModel userModel = new ObjectMapper()
                .readValue(requestContext.getBody(), UserLoginDataModel.class);

        UserRepo userRepo = new UserRepo();

        userRepo.createUser(userModel, userModel.userName.equals("admin"));

        return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

    }
}
