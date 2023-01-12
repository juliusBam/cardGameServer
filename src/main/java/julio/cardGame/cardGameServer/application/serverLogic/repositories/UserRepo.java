package julio.cardGame.cardGameServer.application.serverLogic.repositories;

import julio.cardGame.cardGameServer.application.serverLogic.db.DataTransformation;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.application.serverLogic.models.CompleteUserModel;
import julio.cardGame.cardGameServer.application.serverLogic.models.UserAdditionalDataModel;
import julio.cardGame.cardGameServer.application.serverLogic.models.UserLoginDataModel;
import julio.cardGame.cardGameServer.application.serverLogic.models.UserStatsModel;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRepo {

    public String loginUser(UserLoginDataModel userLoginData) throws SQLException, NoSuchAlgorithmException {

        String sql = """
                        SELECT "authToken"
                            FROM public.users 
                                WHERE "userName"=? 
                                    AND pwd=?
                    """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, userLoginData.userName);
            preparedStatement.setString(2, DataTransformation.calculateHash(userLoginData.password));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
                //return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
            }

            return resultSet.getString(1);

        }

    }

    public CompleteUserModel getUser(String userName) throws SQLException {

        String sql = """
                    SELECT * 
                        FROM public.users 
                        WHERE "userName"=?
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            CompleteUserModel completeUserModel;

            if (resultSet.next()) {
                completeUserModel = new CompleteUserModel(
                        resultSet.getString(2),
                        new UserAdditionalDataModel(resultSet.getString(4), resultSet.getString(6), resultSet.getString(5)),
                        new UserStatsModel(resultSet.getInt(8), resultSet.getInt(9), resultSet.getInt(10)),
                        resultSet.getInt(11), resultSet.getBoolean(13)
                );

                return completeUserModel;

            }

            return null;
        }
    }

    public void updateUser(UserAdditionalDataModel userModel, String requestedUser) throws SQLException {

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

        }

    }

    public boolean checkToken(String token) throws SQLException {

        String sql = """
                SELECT
                    count("userID")
                    FROM users
                        WHERE "authToken"=?;
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, token);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())
                return false;

            return resultSet.getInt(1) == 1;

        }

    }

    public boolean checkTokenBelongsToUser(String token, String userName) throws SQLException {

        String sql = """
                    SELECT
                        "authToken"
                    FROM users
                        WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())
                return false;

            return token.equals(resultSet.getString(1));

        }

    }

    public boolean checkAdmin(String token) throws SQLException {

        String sql = """
                SELECT "isAdmin"
                    FROM users
                        WHERE "authToken"=?;
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, token);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())
                return false;

            return resultSet.getBoolean(1);

        }

    }

    public void createUser(UserLoginDataModel userLoginDataModel, boolean isAdmin) throws SQLException, NoSuchAlgorithmException {

        String sql = """
                        INSERT INTO public.users 
                        ("userID", "userName", pwd, "isAdmin", "authToken") 
                        VALUES (?, ?, ?, ?, ?)
                        """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(UUID.randomUUID()));

            preparedStatement.setString(2, userLoginDataModel.userName);

            preparedStatement.setString(3, DataTransformation.calculateHash(userLoginDataModel.password));

            preparedStatement.setBoolean(4, isAdmin);

            preparedStatement.setString(5, DataTransformation.createAuthToken(userLoginDataModel.userName));

            preparedStatement.execute();

        }

    }

    //Connection has to be provided since we execute a transaction
    public void updateWinsElo(Connection dbConnection, UUID userID, int eloWinner, int eloLooser) throws SQLException {

        String sql = """
                UPDATE
                    users
                        SET "wins"=(SELECT "wins" from users where "userID"='?') + 1 , "elo"=?
                WHERE "userID"='?';
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(userID));
            preparedStatement.setInt(2, DataTransformation.calculateWinnerElo(eloWinner, eloLooser));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(userID));

            preparedStatement.execute();

        }


    }

    //need connection for the transaction
    public void updateLossesElo(Connection dbConnection, UUID userID, int eloLooser, int eloWinner) throws SQLException {

        String sql = """
                UPDATE
                    users
                        SET "losses"=(SELECT "losses" from users where "userID"='?') + 1 , "elo"=?
                WHERE "userID"='?';
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(userID));
            preparedStatement.setInt(2, DataTransformation.calculateLoserElo(eloWinner, eloLooser));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(userID));

            preparedStatement.execute();

        }

    }

}
