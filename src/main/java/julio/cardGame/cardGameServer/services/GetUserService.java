package julio.cardGame.cardGameServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.models.CompleteUserModel;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;

import java.sql.SQLException;

public class GetUserService implements CardGameService {


    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        UserRepo userRepo = new UserRepo();

        String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

        if (requestedUser == null)
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);

        CompleteUserModel userData = userRepo.getUser(requestedUser);

        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(userData);

        return new Response(body, HttpStatus.OK, true);
    }
}
