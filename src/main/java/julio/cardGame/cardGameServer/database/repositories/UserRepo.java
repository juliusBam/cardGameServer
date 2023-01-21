package julio.cardGame.cardGameServer.database.repositories;

import julio.cardGame.cardGameServer.battle.helpers.EloCalculator;
import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.models.*;

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

    private final String stmtLoginUser = """
                        SELECT "authToken"
                            FROM public.users 
                                WHERE "userName"=? 
                                    AND pwd=? AND "active"=true
                    """;

    private final String stmtGetUser = """
                    SELECT "userName", "name", "bio", "image", "elo", "wins", "losses", "coins", "isAdmin", "active"
                        FROM public.users 
                        WHERE "userName"=?
                """;

    private final String stmtUpdateUser = """
                    UPDATE
                        users
                        SET image=?, bio=?, name=?
                            WHERE "userName"=?;
                """;

    private final String stmtCheckToken = """
                SELECT
                    "userName"
                    FROM users
                        WHERE "authToken"=?;
                """;

    private final String stmtCheckTokenBelongsToUser = """
                    SELECT
                        "authToken"
                    FROM users
                        WHERE "userName"=?;
                """;

    private final String stmtCheckAdminByToken = """
                SELECT "isAdmin"
                    FROM users
                        WHERE "authToken"=?;
                """;

    private final String stmtCreateUser = """
                        INSERT INTO public.users 
                            ("userID", "userName", pwd, "isAdmin", "authToken") 
                                VALUES (?, ?, ?, ?, ?)
                        """;

    private final String stmtUpdateWinsElo = """
                        UPDATE
                            users
                                SET "wins"=(SELECT "wins" from users where "userName"=?) + 1 , "elo"=?
                        WHERE "userName"=?;
                """;

    private final String stmtUpdateLossesElo = """
                        UPDATE
                            users
                                SET "losses"=(SELECT "losses" from users where "userName"=?) + 1 , "elo"=?
                        WHERE "userName"=?;
                """;

    private final String stmtFetchDeckCards = """
                        SELECT "cardName", "card_damage", "cardElement", "cardType", "monsterRace"
                            FROM cards
                            WHERE "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?) 
                                AND "deckID"=(SELECT "deckID" FROM users WHERE "userName"=?);
                """;

    private final String stmtCheckIfOwnsCard = """
                    SELECT count("cardID") 
                        FROM cards 
                            WHERE "cardID"=? AND 
                                    "ownerID"=(SELECT "userID" from users WHERE "userName"=?);
                """;

    private final String stmtFetchUserStats = """
                    SELECT wins, losses, elo
                        FROM users
                            WHERE "userName"=?;
                """;

    private final String stmtCheckCardsOwnership = """
                    SELECT distinct COUNT("cardID")
                        FROM cards
                            WHERE ("cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=?) 
                                    AND "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?)
                """;

    private final String stmtAddDeckID = """
                    UPDATE users
                        SET "deckID"=?
                            WHERE "userName"=?;
                """;

    private final String stmtCheckIfUserHasDeck = """
                    SELECT "deckID"
                        FROM users
                            WHERE "userName"=?;
                """;

    private final String stmtCheckUserCoins = """
                    SELECT coins
                        FROM public.users
                        WHERE "userName"=?        
                """;

    private final String stmtUpdateUserCoins = """
                UPDATE users 
                    SET coins=? 
                    WHERE "userName"=?;
                """;

    public void changeUserStatus(String userName, boolean newState) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtChangeUserStatus)) {

            preparedStatement.setBoolean(1, newState);
            preparedStatement.setString(2, userName);

            int changedRecords = preparedStatement.executeUpdate();

        }

    }

    public String loginUser(UserLoginDataModel userLoginData) throws SQLException, NoSuchAlgorithmException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtLoginUser)) {

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

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtGetUser)) {

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

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtUpdateUser)) {

            preparedStatement.setString(1, userModel.image);
            preparedStatement.setObject(2, userModel.bio);
            preparedStatement.setString(3, userModel.name);
            preparedStatement.setString(4, requestedUser);

            int res = preparedStatement.executeUpdate();

        }

    }

    public String checkTokenReturnUser(String token) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtCheckToken)) {

            preparedStatement.setString(1, token);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())
                return null;

            return resultSet.getString(1);

        }

    }

    public boolean checkTokenBelongsToUser(String token, String userName) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtCheckTokenBelongsToUser)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())
                return false;

            return token.equals(resultSet.getString(1));

        }

    }

    public boolean checkAdminByToken(String token) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtCheckAdminByToken)) {

            preparedStatement.setString(1, token);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())
                return false;

            return resultSet.getBoolean(1);

        }

    }

    public void createUser(UserLoginDataModel userLoginDataModel, boolean isAdmin) throws SQLException, NoSuchAlgorithmException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtCreateUser)) {

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

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(stmtUpdateWinsElo)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, EloCalculator.calculateWinnerElo(eloWinner, eloLooser));
            preparedStatement.setString(3, userName);

            int res = preparedStatement.executeUpdate();

        }


    }

    //need connection for the transaction
    public void updateLossesElo(Connection dbConnection, String userName, int eloLooser, int eloWinner) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(stmtUpdateLossesElo)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, EloCalculator.calculateLoserElo(eloWinner, eloLooser));
            preparedStatement.setString(3, userName);

            int res = preparedStatement.executeUpdate();

        }

    }

    public List<CardDeckModel> fetchDeckCards(String userName) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtFetchDeckCards)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userName);

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

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(stmtCheckIfOwnsCard)){

            preparedStatement.setObject(1, DataTransformation.prepareUUID(cardUUID));
            preparedStatement.setString(2, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;

        } catch (SQLException e) {
            throw e;
        }

    }

    public StatsModel fetchUserStats(String userName) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtFetchUserStats)){

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

        int ownedCards = 0;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(stmtCheckCardsOwnership)) {

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

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(stmtAddDeckID)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(newDeckID));
            preparedStatement.setString(2, userName);

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

    public boolean checkIfDeck(Connection dbConn, String userName) throws SQLException, AuthenticationException {

        boolean hasDeck = false;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(stmtCheckIfUserHasDeck)) {

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

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(stmtCheckUserCoins)) {

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

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(stmtUpdateUserCoins)) {

            preparedStatement.setInt(1, userCoins - Constants.PACKAGE_COST);
            preparedStatement.setString(2, userName);

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

}
