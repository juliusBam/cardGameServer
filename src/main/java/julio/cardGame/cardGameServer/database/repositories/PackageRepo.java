package julio.cardGame.cardGameServer.database.repositories;

import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.models.PackageModel;

import java.sql.*;
import java.util.UUID;

public class PackageRepo {

    public void deletePackage(Connection dbConnection, UUID packageID) throws SQLException {

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

    public PackageModel fetchPackage(Connection dbConnection, String userName) throws SQLException {

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
