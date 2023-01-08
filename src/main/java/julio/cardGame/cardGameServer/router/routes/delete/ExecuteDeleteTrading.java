package julio.cardGame.cardGameServer.router.routes.delete;

import julio.cardGame.cardGameServer.application.serverLogic.db.DataTransformation;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.HeadersValidator;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ExecuteDeleteTrading implements Route {
    @Override
    public Response process(RequestContext requestContext) {

        String stringTradeId = requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue());

        String authToken = HeadersValidator.validateToken(requestContext.getHeaders());

        if (authToken == null)
            return new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED);

        if (stringTradeId == null)
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);

        if (HeadersValidator.checkAdmin(authToken)) {

            return deleteAdmin(requestContext, stringTradeId);

        } else {

            return deleteUser(requestContext, stringTradeId, authToken);

        }
    }

    private Response deleteAdmin(RequestContext requestContext, String tradeId) {
        try {

            //validate the uuid we received
            UUID uuid = UUID.fromString(tradeId);

            try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(
                    """
                            DELETE 
                                FROM public.trades 
                                WHERE "tradeID"=?
                        """

            )) {

                preparedStatement.setObject(1, DataTransformation.prepareUUID(uuid));

                preparedStatement.execute();

            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                //throw new RuntimeException(e);
            }


            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private Response deleteUser(RequestContext requestContext, String tradeId, String authToken) {
        try {

            //validate the uuid we received
            UUID uuid = UUID.fromString(tradeId);

            try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(
                    """
                            DELETE 
                                FROM trades 
                                WHERE "tradeID"=? 
                                    AND "userID"=
                                        (SELECT "userID" 
                                            FROM users 
                                            WHERE users."userName"=?)
                        """

            )) {

                preparedStatement.setObject(1, DataTransformation.prepareUUID(uuid));
                preparedStatement.setString(2, HeadersValidator.extractUserName(authToken));

                preparedStatement.execute();

            } catch (SQLException e) {
                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                //throw new RuntimeException(e);
            }


            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
