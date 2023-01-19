package julio.cardGame.cardGameServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.models.PackageModel;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.database.repositories.PackageRepo;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;

import java.sql.Connection;
import java.sql.SQLException;

public class PostTransactionPackagesService implements CardGameService {

    private UserRepo userRepo;
    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        try (Connection dbConnection = DbConnection.getInstance().connect()) {

            this.userRepo = new UserRepo();

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

                dbConnection.rollback();
                return new Response(e);

            }

        }

    }

    private Response buyPackage(Connection dbConnection, String userName, int userCoins) throws SQLException {

        PackageRepo packageRepo = new PackageRepo();

        PackageModel packageData = packageRepo.fetchPackage(dbConnection, userName);

        if (packageData == null || packageData.packageID == null)
            return new Response(DefaultMessages.ERR_NO_PACKAGES.getMessage(), HttpStatus.BAD_REQUEST);

        CardRepo cardRepo = new CardRepo();

        cardRepo.updateCardOwner(dbConnection, userName, packageData);

        packageRepo.deletePackage(dbConnection, packageData.packageID);

        this.userRepo.scaleUserCoin(dbConnection, userName, userCoins);

        return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

    }
}
