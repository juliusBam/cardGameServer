package julio.cardGame.cardGameServer.http.routing.routes.post;

import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.database.repositories.PackageRepo;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.headers.HeadersValidator;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.database.models.PackageModel;

import java.sql.*;

public class ExecutePostTransactionPackage extends AuthenticatedRoute implements Routeable {

    private UserRepo userRepo;

    private PackageRepo packageRepo;

    private CardRepo cardRepo;
    @Override
    public Response process(RequestContext requestContext) {

        AuthorizationWrapper authorizationWrapper;

        try {

            authorizationWrapper = this.requireAuthToken(requestContext.getHeaders());

        } catch (SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        if (authorizationWrapper.response != null)
            return authorizationWrapper.response;

        //extract the connection
        try (Connection dbConnection = DbConnection.getInstance().connect()) {

            this.userRepo = new UserRepo();

            this.packageRepo = new PackageRepo();

            this.cardRepo = new CardRepo();

            int coins = userRepo.checkUsersCoins(dbConnection, authorizationWrapper.userName);

            //no money for pack
            if (coins < Constants.PACKAGE_COST)
                return new Response(DefaultMessages.ERR_NO_COINS.getMessage(), HttpStatus.BAD_REQUEST);

            //begin transaction
            try {

                dbConnection.setAutoCommit(false);

                Response response = this.buyPackage(dbConnection, authorizationWrapper.userName, coins);

                dbConnection.commit();

                return response;

            } catch (SQLException e) {

                try {
                    dbConnection.rollback();

                } catch (SQLException ex) {

                    return new Response(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

                }

                return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

            }

        } catch (SQLException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR.getStatusMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private Response buyPackage(Connection dbConnection, String userName, int userCoins) throws SQLException {

        PackageModel packageData = this.packageRepo.fetchPackage(dbConnection, userName);

        if (packageData == null || packageData.packageID == null)
            return new Response(DefaultMessages.ERR_NO_PACKAGES.getMessage(), HttpStatus.BAD_REQUEST);

        this.cardRepo.updateCardOwner(dbConnection, userName, packageData);

        this.packageRepo.deletePackage(dbConnection, packageData.packageID);

        this.userRepo.scaleUserCoin(dbConnection, userName, userCoins);

        return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

    }

}
