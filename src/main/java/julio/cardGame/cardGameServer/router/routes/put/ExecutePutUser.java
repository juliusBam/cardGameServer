package julio.cardGame.cardGameServer.router.routes.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.common.models.UserAdditionalDataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExecutePutUser extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            UserAdditionalDataModel userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserAdditionalDataModel.class);

            String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

            if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank())
                return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            if (!requestedUser.equals(auth.userName))
                return new Response(DefaultMessages.ERR_MISMATCHING_USERS.getMessage(), HttpStatus.BAD_REQUEST);

            try (Connection connection = DbConnection.getInstance().connect()) {

                this.updateUser(connection, requestedUser, userModel);

                return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void updateUser(Connection connection, String requestedUser, UserAdditionalDataModel userModel) throws SQLException {

        String sql = """
                    UPDATE
                        users
                        SET image=?, bio=?, name=?
                            WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userModel.Image);
            preparedStatement.setObject(2, userModel.Bio);
            preparedStatement.setString(3, userModel.Name);
            preparedStatement.setString(4, requestedUser);

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }
}
