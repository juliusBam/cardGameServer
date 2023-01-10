package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.HeadersValidator;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.common.models.CompleteUserModel;
import julio.cardGame.common.models.UserAdditionalDataModel;
import julio.cardGame.common.models.UserStats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteGetUser implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

        if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank()) {
            return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String authToken = HeadersValidator.validateToken(requestContext.getHeaders());

        if (authToken == null)
            return new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED);

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(
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
                        new UserStats(resultSet.getInt(8), resultSet.getInt(9),resultSet.getInt(10)),
                        resultSet.getInt(11)
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
        }



    }
}
