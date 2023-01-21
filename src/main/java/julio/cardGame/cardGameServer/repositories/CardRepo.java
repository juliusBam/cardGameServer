package julio.cardGame.cardGameServer.repositories;

import julio.cardGame.cardGameServer.database.DataTransformation;
import julio.cardGame.cardGameServer.database.DbConnection;
import julio.cardGame.cardGameServer.models.CardDbModel;
import julio.cardGame.cardGameServer.models.PackageModel;
import julio.cardGame.cardGameServer.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardRepo {

    private final String stmtFetchUserCards = """
                SELECT *
                    FROM cards
                        WHERE "ownerID"=(SELECT "userID"
                                            FROM users
                                                WHERE "userName"=?);
            """;

    private final String stmtAssignCardsToDeck = """
            UPDATE cards
                 SET "deckID"=?
                         WHERE "cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=?;
             """;

    private final String stmtInsertNewCards = """
                INSERT INTO
                    cards("cardID", "cardName", "card_damage", "cardElement", "cardType", "monsterRace")
                VALUES
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?);
            """;

    private final String stmtUpdateCardOwner = """
                UPDATE cards
                    SET "ownerID"=(SELECT "userID" from users WHERE "userName"=?)
                        WHERE 
                            "cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=?;
            """;

    private final String stmtChangeOwnershipOfferedCard = """
                UPDATE cards
                    SET "deckID"=null, "ownerID"=(SELECT "userID" FROM trades WHERE "tradeID"=?)
                        WHERE "cardID"=?;
            """;

    private final String getStmtChangeOwnershipCardInTrade = """
                UPDATE cards
                    SET "deckID"=null, "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?)
                        WHERE "cardID"=(SELECT "offeredCardID" FROM trades WHERE "tradeID"=?);
            """;

    private final String getStmtCardIsInDeck = """
                SELECT count("cardID")
                    FROM cards
                        WHERE "cardID"=? AND "deckID"=null
            """;

    private final String getStmtCheckCardInTrade = """
                SELECT
                    count("offeredCardID")
                        FROM trades WHERE "offeredCardID"=? OR "offeredCardID"=? OR "offeredCardID"=? OR "offeredCardID"=?
            """;

    public List<CardDbModel> fetchUserCards(String userName) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtFetchUserCards)) {


            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<CardDbModel> cards = new ArrayList<>();

            while (resultSet.next()) {
                cards.add(
                        new CardDbModel(
                                resultSet.getObject(1, UUID.class),
                                resultSet.getObject(2, UUID.class),
                                resultSet.getObject(3, UUID.class),
                                resultSet.getString(4),
                                resultSet.getDouble(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8)
                        )
                );

            }

            return cards;

        }

    }

    public void moveCardsToDeck(Connection dbConn, UUID newDeckID, List<UUID> cardIds) throws SQLException {

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(stmtAssignCardsToDeck)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(newDeckID));

            for (int i = 0; i < Constants.DECK_SIZE; i++) {
                preparedStatement.setObject(i + 2, DataTransformation.prepareUUID(cardIds.get(i)));
            }

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    public void addCards(Connection dbConnection, List<CardDbModel> cards) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(stmtInsertNewCards)) {

            for (int i = 0; i < 5; ++i) {

                preparedStatement.setObject(1 + (i * 6), DataTransformation.prepareUUID(cards.get(i).cardID));
                preparedStatement.setString(2 + (i * 6), cards.get(i).cardName);
                preparedStatement.setDouble(3 + (i * 6), cards.get(i).card_damage);
                preparedStatement.setObject(4 + (i * 6), cards.get(i).cardElement, Types.OTHER);
                preparedStatement.setObject(5 + (i * 6), cards.get(i).cardType, Types.OTHER);
                preparedStatement.setObject(6 + (i * 6), cards.get(i).monsterRace, Types.OTHER);

            }

            preparedStatement.execute();

        }

    }

    public void updateCardOwner(Connection dbConnection, String userName, PackageModel packageData) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(stmtUpdateCardOwner)) {

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

    public void changeOwnershipOfferedCard(Connection dbConnection, UUID tradeUUID, UUID cardUUID) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(stmtChangeOwnershipOfferedCard)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(cardUUID));

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    public void changeOwnershipCardInTrade(Connection dbConnection, String userName, UUID tradeUUID) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getStmtChangeOwnershipCardInTrade)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setObject(2, DataTransformation.prepareUUID(tradeUUID));

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    //returns true if the card is in a deck
    public boolean checkIfCardInDeck(Connection dbConnection, UUID cardToTrade) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getStmtCardIsInDeck)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(cardToTrade));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                return count != 1;

            } else {

                return true;

            }

        }

    }

    public int checkIfCardInDeck(List<UUID> cardIds) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(getStmtCheckCardInTrade)){

            preparedStatement.setObject(1, DataTransformation.prepareUUID(cardIds.get(0)));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(cardIds.get(1)));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(cardIds.get(2)));
            preparedStatement.setObject(4, DataTransformation.prepareUUID(cardIds.get(3)));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return resultSet.getInt(1);

            } else {

                return 1;

            }

        }

    }
}
