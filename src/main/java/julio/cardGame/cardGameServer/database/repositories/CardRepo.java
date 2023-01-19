package julio.cardGame.cardGameServer.database.repositories;

import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.models.CardDbModel;
import julio.cardGame.cardGameServer.database.models.PackageModel;
import julio.cardGame.cardGameServer.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardRepo {

    public List<CardDbModel> fetchUserCards(String userName) throws SQLException {

        String sql = """
                SELECT * 
                    FROM cards 
                        WHERE "ownerID"=(SELECT "userID" 
                                            FROM users 
                                                WHERE "userName"=?);
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {


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

        String sql = """
               UPDATE cards
                    SET "deckID"=?
                            WHERE "cardID"=? OR "cardID"=? OR "cardID"=? OR "cardID"=?;
                """;

        try (PreparedStatement preparedStatement = dbConn.prepareStatement(sql)){

            preparedStatement.setObject(1, DataTransformation.prepareUUID(newDeckID));

            for (int i = 0; i < Constants.DECK_SIZE; i++) {
                preparedStatement.setObject(i + 2, DataTransformation.prepareUUID(cardIds.get(i)));
            }

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

    public void addCards(Connection dbConnection, List<CardDbModel> cards) throws SQLException {

        String sqlInsertCards = """
                INSERT INTO
                    cards("cardID", "cardName", "card_damage", "cardElement", "cardType", "monsterRace")
                VALUES
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?),
                    (?,?,?,?,?,?);
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlInsertCards)) {

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

    public void createPackage(Connection dbConnection, List<UUID> cardsIDs) throws SQLException {

        String sqlInsertPackage = """
                INSERT INTO public.packages
                    VALUES (?,?,?,?,?,?);
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlInsertPackage)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(UUID.randomUUID()));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(cardsIDs.get(0)));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(cardsIDs.get(1)));
            preparedStatement.setObject(4, DataTransformation.prepareUUID(cardsIDs.get(2)));
            preparedStatement.setObject(5, DataTransformation.prepareUUID(cardsIDs.get(3)));
            preparedStatement.setObject(6, DataTransformation.prepareUUID(cardsIDs.get(4)));

            preparedStatement.execute();

        }

    }

    public void updateCardOwner(Connection dbConnection, String userName, PackageModel packageData) throws SQLException {

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

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

    public void changeOwnershipOfferedCard(Connection dbConnection, UUID tradeUUID, UUID cardUUID) throws SQLException {

        String sql = """
                    UPDATE cards
                        SET "deckID"=null, "ownerID"=(SELECT "userID" FROM trades WHERE "tradeID"=?)
                        WHERE "cardID"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(cardUUID));

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

    public void changeOwnershipCardInTrade(Connection dbConnection, String userName, UUID tradeUUID) throws SQLException {

        String sql = """
                UPDATE cards
                    SET "deckID"=null, "ownerID"=(SELECT "userID" FROM users WHERE "userName"=?)
                        WHERE "cardID"=(SELECT "offeredCardID" FROM trades WHERE "tradeID"=?);
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setObject(2, DataTransformation.prepareUUID(tradeUUID));

            int res = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

}
