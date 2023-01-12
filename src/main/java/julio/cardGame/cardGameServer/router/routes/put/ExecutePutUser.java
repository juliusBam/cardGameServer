package julio.cardGame.cardGameServer.router.routes.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.application.serverLogic.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.HeadersValidator;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.cardGameServer.application.serverLogic.models.UserAdditionalDataModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExecutePutUser extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            UserRepo userRepo = new UserRepo();

            String requestedUser = requestContext.fetchParameter(RequestParameters.USERNAME.getParamValue());

            String authToken = HeadersValidator.validateToken(requestContext.getHeaders());

            if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank())
                return new Response(DefaultMessages.ERR_NO_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            if (!this.canAccessUserData(requestContext.getHeaders(), requestedUser))
                return new Response(DefaultMessages.ERR_MISMATCHING_USERS.getMessage(), HttpStatus.BAD_REQUEST);

            UserAdditionalDataModel userModel = new ObjectMapper()
                    .readValue(requestContext.getBody(), UserAdditionalDataModel.class);



            if (!requestedUser.equals(requestedUser))
                return new Response(DefaultMessages.ERR_MISMATCHING_USERS.getMessage(), HttpStatus.BAD_REQUEST);

            //this.updateUser(requestedUser, userModel);
            userRepo.updateUser(userModel, requestedUser);

            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);


        } catch (JsonProcessingException e) {
            return new Response(DefaultMessages.ERR_JSON_PARSE_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void updateUser(String requestedUser, UserAdditionalDataModel userModel) throws SQLException {

        String sql = """
                    UPDATE
                        users
                        SET image=?, bio=?, name=?
                            WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, userModel.image);
            preparedStatement.setObject(2, userModel.bio);
            preparedStatement.setString(3, userModel.name);
            preparedStatement.setString(4, requestedUser);

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }
}
