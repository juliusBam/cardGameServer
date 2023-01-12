package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;

import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.cardGameServer.application.dbLogic.models.CompleteUserModel;

import java.sql.SQLException;

public class ExecuteGetUser extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        /*String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

        if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank()) {
            return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String authToken = HeadersValidator.validateToken(requestContext.getHeaders());

        if (authToken == null)
            return new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED);

        /*try {

            CompleteUserModel userData = new UserRepo().getUser(requestedUser);

            String body = new ObjectMapper()
                    .writeValueAsString(userData);

            return new Response(body, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }  catch (SQLException e) {
            return new Response(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }*/

        /*try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(
                """
                        SELECT * 
                            FROM public.users 
                            WHERE "userName"=?
                    """

        )) {

            preparedStatement.setString(1, requestedUser);

            ResultSet resultSet = preparedStatement.executeQuery();

            CompleteUserModel completeUserModel;

            if (resultSet.next()) {
                completeUserModel = new CompleteUserModel(
                        resultSet.getString(2),
                        new UserAdditionalDataModel(resultSet.getString(4), resultSet.getString(6), resultSet.getString(5)),
                        new UserStatsModel(resultSet.getInt(8), resultSet.getInt(9),resultSet.getInt(10)),
                        resultSet.getInt(11), resultSet.getBoolean(13)
                );

                try {
                    String body = new ObjectMapper()
                            .writeValueAsString(completeUserModel);

                    return new Response(body,HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new Response(HttpStatus.INTERNAL_SERVER_ERROR.getStatusMessage(), HttpStatus.INTERNAL_SERVER_ERROR);



        } catch (SQLException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            //throw new RuntimeException(e);
        }*/

        try {

            String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

            if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank())
                return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            if (!this.canAccessUserData(requestContext.getHeaders(), requestedUser))
                return new Response(DefaultMessages.ERR_MISMATCHING_USERS.getMessage(), HttpStatus.BAD_REQUEST);

            CompleteUserModel userData = new UserRepo().getUser(requestedUser);

            String body = new ObjectMapper()
                    .writeValueAsString(userData);

            return new Response(body, HttpStatus.OK);

        } catch (SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
