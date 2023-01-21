package julio.cardGame.cardGameServer.database.repositories;

import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.models.PackageModel;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class PackageRepo {

    private final String stmtDeletePkg = """
            DELETE 
                FROM packages 
                    WHERE "packageID"=?;
            """;

    private final String stmtFetchPkg = """
            SELECT * 
                FROM "packages"
                LIMIT 1;
            """;

    private final String stmtInsertPkg = """
            INSERT INTO public.packages
                VALUES (?,?,?,?,?,?);
            """;

    public void deletePackage(Connection dbConnection, UUID packageID) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(this.stmtDeletePkg)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(packageID));

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    public PackageModel fetchPackage(Connection dbConnection, String userName) throws SQLException {

        try (Statement statement = dbConnection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(stmtFetchPkg);

            PackageModel packageModel = null;

            if (resultSet.next()) {
                packageModel = new PackageModel(
                        resultSet.getObject(1, UUID.class),
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

    public void insertNewPackage(Connection dbConnection, List<UUID> cardsIDs) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(this.stmtInsertPkg)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(UUID.randomUUID()));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(cardsIDs.get(0)));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(cardsIDs.get(1)));
            preparedStatement.setObject(4, DataTransformation.prepareUUID(cardsIDs.get(2)));
            preparedStatement.setObject(5, DataTransformation.prepareUUID(cardsIDs.get(3)));
            preparedStatement.setObject(6, DataTransformation.prepareUUID(cardsIDs.get(4)));

            preparedStatement.execute();

        }

    }

}
