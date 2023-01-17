package julio.cardGame.cardGameServer.database.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.models.TradeModel;
import julio.cardGame.cardGameServer.database.models.TradeViewModel;
import julio.cardGame.cardGameServer.battle.cards.CardTypes;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeRepo {

    public void insertNewTradeDeal(Connection dbConnection, String userName, TradeModel tradeModel, CardTypes requiredType) throws SQLException {

        String sql = """
                            INSERT INTO trades
                                VALUES (?,?,?, (SELECT "userID" FROM users where "userName"=?), ?);
                        """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeModel.id));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(tradeModel.cardToTrade));
            preparedStatement.setInt(3, tradeModel.minimumDamage);
            preparedStatement.setString(4, userName);
            preparedStatement.setObject(5, requiredType.getCardType(), Types.OTHER);

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }


    }

    public void deleteTrade(Connection dbConnection, UUID tradeUUID) throws SQLException {

        String sql = """
                DELETE
                    FROM trades
                        WHERE "tradeID"=?;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)){

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));

            preparedStatement.execute();

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

            preparedStatement.execute();

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

            preparedStatement.execute();

        } catch (SQLException e) {
            throw e;
        }

    }

    public boolean checkIfCardFulfillsTradeReq(Connection dbConnection, UUID offeredCard, UUID tradeUUID) throws SQLException {

        String sql = """
                        SELECT count("tradeID")
                            FROM trades
                                WHERE "tradeID"=?
                                    AND "requiredCardType"=(SELECT "cardType" FROM cards WHERE "cardID"=?)
                                    AND "minimumDamage"<(SELECT card_damage FROM cards WHERE "cardID"=?);
                    """;


        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(offeredCard));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(offeredCard));

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;

        } catch (Exception e) {
            throw e;
        }

    }

    public boolean checkIfSelfTrade(Connection dbConnection, UUID tradeUUID, String userName) throws SQLException {

        String sqlFindTradeOwner = """
                    SELECT count("tradeID")
                        FROM trades
                            WHERE "tradeID"=? AND "userID"=(SELECT "userID" FROM users WHERE "userName"=?);
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlFindTradeOwner)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setString(2, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            throw e;
        }

    }

    public List<TradeViewModel> fetchTrades() throws SQLException {

        String sql = """
                        SELECT t.* 
                            FROM public."tradesMetaData" t;
                    """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<TradeViewModel> trades = new ArrayList<>();

            while (resultSet.next()) {
                trades.add(
                        new TradeViewModel(
                                resultSet.getString(1),
                                resultSet.getInt(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getDouble(6)
                        )
                );
            }

            return trades;

        } catch (SQLException e) {
            throw e;
        }


    }

}
