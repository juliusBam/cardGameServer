package julio.cardGame.cardGameServer.application.serverLogic.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.serverLogic.db.DataTransformation;
import julio.cardGame.cardGameServer.application.serverLogic.db.DbConnection;
import julio.cardGame.cardGameServer.application.serverLogic.models.CompleteUserModel;
import julio.cardGame.cardGameServer.application.serverLogic.models.UserAdditionalDataModel;
import julio.cardGame.cardGameServer.application.serverLogic.models.UserLoginDataModel;
import julio.cardGame.cardGameServer.application.serverLogic.models.UserStatsModel;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;

import java.security.NoSuchAlgorithmException;
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

    public void createAdmin(UserLoginDataModel userLoginDataModel) throws SQLException {

    }

    public void createNormalUser(UserLoginDataModel userLoginDataModel) throws SQLException {

    }

    public void updateWinsElo(UUID userID, int eloWinner, int eloLooser) throws SQLException {

    }

    public void updateLossesElo(UUID userID, int eloLooser, int eloWinner) throws SQLException {

    }

}
