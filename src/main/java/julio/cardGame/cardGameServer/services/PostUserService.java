package julio.cardGame.cardGameServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.models.UserLoginDataModel;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;

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
