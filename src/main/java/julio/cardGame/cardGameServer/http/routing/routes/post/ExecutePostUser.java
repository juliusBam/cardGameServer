package julio.cardGame.cardGameServer.http.routing.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.database.models.UserLoginDataModel;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ExecutePostUser implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        try {

            UserLoginDataModel userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserLoginDataModel.class);

            new UserRepo().createUser(userModel, userModel.userName.equals("admin"));

            return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException | NoSuchAlgorithmException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

