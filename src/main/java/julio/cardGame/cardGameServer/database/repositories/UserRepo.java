package julio.cardGame.cardGameServer.database.repositories;

import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.models.*;
import julio.cardGame.cardGameServer.Constants;

import javax.naming.AuthenticationException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepo {

    private final String stmtChangeUserStatus = """
                UPDATE users
                    SET "active"=? WHERE "userName"=?
            """;

    public void changeUserStatus(String userName, boolean newState) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtChangeUserStatus)) {

            preparedStatement.setBoolean(1, newState);
            preparedStatement.setString(2, userName);

            int changedRecords = preparedStatement.executeUpdate();

        }

    }

    public String loginUser(UserLoginDataModel userLoginData) throws SQLException, NoSuchAlgorithmException {

        String sql = """
                        SELECT "authToken"
                            FROM public.users 
                                WHERE "userName"=? 
                                    AND pwd=? AND "active"=true
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
                    SELECT "userName", "name", "bio", "image", "elo", "wins", "losses", "coins", "isAdmin", "active"
                        FROM public.users 
                        WHERE "userName"=?
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            CompleteUserModel completeUserModel;

            if (resultSet.next()) {
                completeUserModel = new CompleteUserModel(
                        resultSet.getString(1),
                        new UserAdditionalDataModel(
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4)
                        ),
                        new UserStatsModel(
                                resultSet.getInt(5),
                                resultSet.getInt(6),
                                resultSet.getInt(7)
                        ),
                        resultSet.getInt(8),
                        resultSet.getBoolean(9),
                        resultSet.getBoolean(10)
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

            int res = preparedStatement.executeUpdate();

        }

    }

    public String checkTokenReturnUser(String token) throws SQLException {

        String sql = """
                SELECT
                    "userName"
                    FROM users
                        WHERE "authToken"=?;
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, token);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())
                return null;

            return resultSet.getString(1);

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

    public boolean checkAdminByToken(String token) throws SQLException {

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
    public void updateWinsElo(Connection dbConnection, String userName, int eloWinner, int eloLooser) throws SQLException {

        String sql = """
                UPDATE
                    users
                        SET "wins"=(SELECT "wins" from users where "userName"=?) + 1 , "elo"=?
                WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, DataTransformation.calculateWinnerElo(eloWinner, eloLooser));
            preparedStatement.setString(3, userName);

            int res = preparedStatement.executeUpdate();

        }


    }

    //need connection for the transaction
    public void updateLossesElo(Connection dbConnection, String userName, int eloLooser, int eloWinner) throws SQLException {

        String sql = """
                UPDATE
                    users
                        SET "losses"=(SELECT "losses" from users where "userName"=?) + 1 , "elo"=?
                WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, DataTransformation.calculateLoserElo(eloWinner, eloLooser));
            preparedStatement.setString(3, userName);

            int res = preparedStatement.executeUpdate();

        }

    }

    public List<CardDeckModel> fetchDeckCards(String userName) throws SQLException {

        String sql = """
                        SELECT "cardName", "card_damage", "cardElement", "cardType", "monsterRace"
                            FROM cards
                            WHERE "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?) 
                                AND "deckID"=(SELECT "deckID" FROM users WHERE "userName"=?);
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            return this.parseFetchDeckResult(resultSet);

        } catch (SQLException e) {
            throw e;
        }

    }

    //used by fetch deck??
    public List<CardDeckModel> fetchDeckCards(UUID userID) throws SQLException {

        String sql = """
                        SELECT "cardName", "card_damage", "cardElement", "cardType", "monsterRace"
                            FROM cards
                            WHERE "ownerID"=(SELECT "userID" FROM users WHERE "userID"=?) 
                                AND "deckID"=(SELECT "deckID" FROM users WHERE "userID"=?);
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(userID));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(userID));

            ResultSet resultSet = preparedStatement.executeQuery();

            return this.parseFetchDeckResult(resultSet);

        } catch (SQLException e) {
            throw e;
        }

    }

    protected List<CardDeckModel> parseFetchDeckResult(ResultSet fetchDeckResult) throws SQLException {

        List<CardDeckModel> deck = new ArrayList<>();

        while (fetchDeckResult.next()) {

            deck.add(
                    new CardDeckModel(
                            fetchDeckResult.getString(1),
                            fetchDeckResult.getDouble(2),
                            fetchDeckResult.getString(3),
                            fetchDeckResult.getString(4),
                            fetchDeckResult.getString(5)
                    )
            );

        }

        return deck;
    }

    public boolean checkIfOwnsCard(Connection dbConnection, String userName, UUID cardUUID) throws SQLException {

        String sql = """
                    SELECT count("cardID") 
                        FROM cards 
                            WHERE "cardID"=? AND 
                                    "ownerID"=(SELECT "userID" from users WHERE "userName"=?);
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)){

            preparedStatement.setObject(1, DataTransformation.prepareUUID(cardUUID));
            preparedStatement.setString(2, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;

        } catch (SQLException e) {
            throw e;
        }

    }

    public StatsModel fetchUserStats(String userName) throws SQLException {

        String sql = """
                    SELECT wins, losses, elo
                        FROM users
                            WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)){

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                preparedStatement.close();
                return null;
            }

            int wins = resultSet.getInt(1);
            int losses = resultSet.getInt(2);
            double winRate = DataTransformation.calculateWinRate(wins, losses);

            return new StatsModel(
                    wins,
                    losses,
                    winRate,
                    resultSet.getInt(3)
            );

        }


    }

    public int checkCardsOwnership(Connection dbConn, List<UUID> cardIds, String userName) throws SQLException {

        String sql = """
                    SELECT distinct COUNT("cardID")
                        FROM cards
                            WHERE ("cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=?) 
                                    AND "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?)
                """;

        int ownedCards = 0;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {

            for (int i = 0; i < Constants.DECK_SIZE; ++i) {
                preparedStatement.setObject(i+1, DataTransformation.prepareUUID(cardIds.get(i)));
            }

            preparedStatement.setString(Constants.DECK_SIZE + 1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ownedCards = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw e;
        }

        return ownedCards;

    }

    public void addDeckID(Connection dbConn, UUID newDeckID, String userName) throws SQLException {

        String sql = """
                    UPDATE users
                        SET "deckID"=?
                            WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(newDeckID));
            preparedStatement.setString(2, userName);

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

    public boolean checkIfDeck(Connection dbConn, String userName) throws SQLException, AuthenticationException {

        String sql = """
                    SELECT "deckID"
                        FROM users
                            WHERE "userName"=?;
                """;

        boolean hasDeck = false;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                UUID deckID = resultSet.getObject(1, UUID.class);

                if (deckID != null)
                    hasDeck = true;

            } else {
                preparedStatement.close();
                throw new AuthenticationException("User does not exist");
            }

        }

        return hasDeck;

    }

    public int checkUsersCoins(Connection dbConnection, String userName) throws SQLException {

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

            return userCoins;

        }

    }

    public void scaleUserCoin(Connection dbConnection, String userName, int userCoins) throws SQLException {

        String sql = """
                UPDATE users 
                    SET coins=? 
                    WHERE "userName"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userCoins - Constants.PACKAGE_COST);
            preparedStatement.setString(2, userName);

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

    public void updateUser(String requestedUser, UserAdditionalDataModel userModel) throws SQLException {

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

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }


}
