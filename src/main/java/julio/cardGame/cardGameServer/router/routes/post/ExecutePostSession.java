package julio.cardGame.cardGameServer.router.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DataTransformation;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.UserLoginDataModel;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecutePostSession implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        UserLoginDataModel userModel = null;

        try {

            userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserLoginDataModel.class);

            try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(
                    """
                            SELECT "userName" 
                                FROM public.users 
                                    WHERE "userName"=? 
                                        AND pwd=?
                        """

            )) {

                preparedStatement.setString(1, userModel.Username);
                preparedStatement.setString(2, DataTransformation.calculateHash(userModel.Password));


                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
                }

                String userName = resultSet.getString(1);

                return new Response(userName, HttpStatus.OK);

            } catch (SQLException | NoSuchAlgorithmException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                //throw new RuntimeException(e);
            }

        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

