package julio.cardGame.cardGameServer.router.routes.post;

import julio.cardGame.cardGameServer.application.serverLogic.db.DataTransformation;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.http.HeadersValidator;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.Constants;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.PackageModel;

import java.sql.*;
import java.util.UUID;

public class ExecutePostTransactionPackage implements Routeable {

    @Override
    public Response process(RequestContext requestContext) {

        //todo refactor
        String authToken = HeadersValidator.validateToken(requestContext.getHeaders());

        if (authToken == null)
            return new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED);

        String userName = HeadersValidator.extractUserName(authToken);

        if (userName == null)
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);

        //extract the connection
        try (Connection dbConnection = DbConnection.getInstance().connect()) {

            int coins = this.checkUsersCoins(dbConnection, userName);

            //no money for pack
            if (coins < Constants.PACKAGE_COST)
                return new Response(DefaultMessages.ERR_NO_COINS.getMessage(), HttpStatus.BAD_REQUEST);

            //begin transaction
            try {

                dbConnection.setAutoCommit(false);

                Response response = this.buyPackage(dbConnection, userName, coins);

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


    private int checkUsersCoins(Connection dbConnection, String userName) throws SQLException {

        String sqlCheckCoins =
                """
                    SELECT coins
                        FROM public.users
                        WHERE "userName"=?        
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCheckCoins)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            int userCoins = 0;

            if (resultSet.next()) {
                userCoins = resultSet.getInt(1);
            }

            preparedStatement.close();
            return userCoins;

        } catch (SQLException e) {
            throw e;
        }

    }

    private Response buyPackage(Connection dbConnection, String userName, int userCoins) throws SQLException {

        PackageModel packageData = this.fetchPackage(dbConnection, userName);

        if (packageData == null || packageData.packageID == null)
            return new Response(DefaultMessages.ERR_NO_PACKAGES.getMessage(), HttpStatus.BAD_REQUEST);

        this.updateCardOwner(dbConnection, userName, packageData);

        this.deletePackage(dbConnection, packageData.packageID);

        this.scaleUserCoin(dbConnection, userName, userCoins);

        return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

    }

    private void scaleUserCoin(Connection dbConnection, String userName, int userCoins) throws SQLException {

        String sql = """
                UPDATE users 
                    SET coins=? 
                    WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userCoins - Constants.PACKAGE_COST);
            preparedStatement.setString(2, userName);

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    private void deletePackage(Connection dbConnection, UUID packageID) throws SQLException {

        String sql = """
                DELETE 
                    FROM packages 
                        WHERE "packageID"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(packageID));

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    private void updateCardOwner(Connection dbConnection, String userName, PackageModel packageData) throws SQLException {

        String sql = """
                UPDATE cards
                    SET "ownerID"=(SELECT "userID" from users WHERE "userName"=?)
                    WHERE 
                        "cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setObject(2, DataTransformation.prepareUUID(packageData.firstCardID));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(packageData.secondCardID));
            preparedStatement.setObject(4, DataTransformation.prepareUUID(packageData.thirdCardID));
            preparedStatement.setObject(5, DataTransformation.prepareUUID(packageData.fourthCardID));
            preparedStatement.setObject(6, DataTransformation.prepareUUID(packageData.fifthCardID));

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    private PackageModel fetchPackage(Connection dbConnection, String userName) throws SQLException {

        String sql = """
                SELECT * 
                    FROM "packages"
                    LIMIT 1;
                """;

        try (Statement statement = dbConnection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            PackageModel packageModel = null;

            if (resultSet.next()) {
                packageModel = new PackageModel(
                    resultSet.getObject(1,UUID.class),
                    resultSet.getObject(2, UUID.class),
                        resultSet.getObject(3, UUID.class),
                        resultSet.getObject(4, UUID.class),
                        resultSet.getObject(5, UUID.class),
                        resultSet.getObject(6, UUID.class)
                );
            }

            statement.close();
            return packageModel;

        } catch (SQLException e) {
            throw e;
        }

    }
}
