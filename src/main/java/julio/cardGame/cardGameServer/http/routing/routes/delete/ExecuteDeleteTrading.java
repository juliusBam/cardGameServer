package julio.cardGame.cardGameServer.http.routing.routes.delete;

import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.communication.headers.HeadersValidator;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class ExecuteDeleteTrading extends AuthenticatedRoute implements Routeable {


    @Override
    public Response process(RequestContext requestContext) {

        try {

            String stringTradeId = requestContext.fetchParameter(RequestParameters.SELECTED_TRADE_DEAL.getParamValue());

            String userName;

            AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            userName = auth.userName;

            if (stringTradeId == null)
                return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);

            //if the user is an admin we do not have to check that the trade belongs to him
            if (this.requireAdmin(requestContext.getHeaders()).isAdmin) {

                return deleteAdmin(requestContext, stringTradeId);

            } else {

                return deleteUser(requestContext, stringTradeId, userName);

            }

        } catch (SQLException e) {
            //return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new Response(e);
        }
    }

    private Response deleteAdmin(RequestContext requestContext, String tradeId) throws SQLException {
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

            }
            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private Response deleteUser(RequestContext requestContext, String tradeId, String userName) throws SQLException {
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
                preparedStatement.setString(2, userName);

                preparedStatement.execute();

            }

            return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
