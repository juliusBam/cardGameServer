package julio.cardGame.cardGameServer.router.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.application.serverLogic.db.DataTransformation;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.UserLoginDataModel;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class ExecutePostUser implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        UserLoginDataModel userModel = null;

        try {

            userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserLoginDataModel.class);

            try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(
                    """
                        INSERT INTO public.users 
                        ("userID", "userName", pwd) 
                        VALUES (?, ?, ?)
                        """

            )) {

                preparedStatement.setObject(1, DataTransformation.prepareUUID(UUID.randomUUID()));

                preparedStatement.setString(2, userModel.Username);

                preparedStatement.setString(3, DataTransformation.calculateHash(userModel.Password));

                preparedStatement.execute();

            } catch (SQLException | NoSuchAlgorithmException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                //throw new RuntimeException(e);
            }

            //todo db logic

            return new Response(HttpStatus.CREATED.getStatusMessage(), HttpStatus.CREATED);

        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

