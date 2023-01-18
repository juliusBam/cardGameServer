package julio.cardGame.cardGameServer.http.routing.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.database.models.UserLoginDataModel;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ExecutePostSession implements Routeable {

    private final UserRepo userRepo;

    public ExecutePostSession() {
        this.userRepo = new UserRepo();
    }

    @Override
    public Response process(RequestContext requestContext) {

        try {

            UserLoginDataModel userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserLoginDataModel.class);

            String token = this.userRepo.loginUser(userModel);

            if (token == null)
                return new Response(DefaultMessages.ERR_NO_FOUND_USER.getMessage(), HttpStatus.BAD_REQUEST);

            return new Response(token, HttpStatus.OK);

        } catch (SQLException e) {

            return new Response(e);

        } catch (NoSuchAlgorithmException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), e);

        }

    }
}

