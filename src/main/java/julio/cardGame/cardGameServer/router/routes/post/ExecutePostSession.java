package julio.cardGame.cardGameServer.router.routes.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.cardGameServer.application.dbLogic.models.UserLoginDataModel;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ExecutePostSession implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        try {

            UserLoginDataModel userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserLoginDataModel.class);

            UserRepo userRepo = new UserRepo();

            String token = userRepo.loginUser(userModel);

            if (token == null)
                return new Response(DefaultMessages.ERR_NO_FOUND_USER.getMessage(), HttpStatus.BAD_REQUEST);

            return new Response(token, HttpStatus.OK);

            /*try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(
                    """
                        SELECT "authToken"
                            FROM public.users 
                                WHERE "userName"=? 
                                    AND pwd=?
                        """

            )) {

                preparedStatement.setString(1, userModel.userName);
                preparedStatement.setString(2, DataTransformation.calculateHash(userModel.password));


                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
                }

                String authToken = resultSet.getString(1);

                return new Response(authToken, HttpStatus.OK);

            } catch (SQLException | NoSuchAlgorithmException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                //throw new RuntimeException(e);
            }*/

        } catch (SQLException | NoSuchAlgorithmException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

