package julio.cardGame.cardGameServer.database.repositories;

import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.models.TradeModel;
import julio.cardGame.cardGameServer.database.models.TradeViewModel;
import julio.cardGame.cardGameServer.battle.cards.CardTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeRepo {

    private final String stmtInsertNewTrade = """
                INSERT INTO trades
                    VALUES (?,?,?, (SELECT "userID" FROM users where "userName"=?), ?);
            """;

    private final String stmtDeleteTraded = """
            DELETE
                FROM trades
                    WHERE "tradeID"=?;
            """;

    private final String stmtCompareCardTradeRequirements = """
                SELECT count("tradeID")
                    FROM trades
                        WHERE "tradeID"=?
                            AND "requiredCardType"=(SELECT "cardType" FROM cards WHERE "cardID"=?)
                            AND "minimumDamage"<(SELECT card_damage FROM cards WHERE "cardID"=?);
            """;

    private final String stmtCheckSelfTrade = """
                SELECT count("tradeID")
                    FROM trades
                        WHERE "tradeID"=? AND "userID"=(SELECT "userID" FROM users WHERE "userName"=?);
            """;

    private final String stmtFetchTrades = """
                SELECT t.* 
                    FROM public."tradesMetaData" t;
            """;

    private final String stmtDeleteTradeNormalUser = """
                DELETE 
                    FROM trades 
                    WHERE "tradeID"=? 
                        AND "userID"=
                            (SELECT "userID" 
                                FROM users 
                                WHERE users."userName"=?)
            """;

    private final String stmtDeleteTradeAdmin = """
                DELETE 
                    FROM public.trades 
                    WHERE "tradeID"=?
            """;

    public void insertNewTradeDeal(Connection dbConnection, String userName, TradeModel tradeModel, CardTypes requiredType) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(this.stmtInsertNewTrade)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeModel.id));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(tradeModel.cardToTrade));
            preparedStatement.setInt(3, tradeModel.minimumDamage);
            preparedStatement.setString(4, userName);
            preparedStatement.setObject(5, requiredType.getCardType(), Types.OTHER);

            preparedStatement.execute();

        }

    }

    public void deleteTrade(Connection dbConnection, UUID tradeUUID) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(this.stmtDeleteTraded)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));

            preparedStatement.execute();

        }

    }

    public boolean checkIfCardFulfillsTradeReq(Connection dbConnection, UUID offeredCard, UUID tradeUUID) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(this.stmtCompareCardTradeRequirements)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setObject(2, DataTransformation.prepareUUID(offeredCard));
            preparedStatement.setObject(3, DataTransformation.prepareUUID(offeredCard));

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) == 1;

        }

    }

    public boolean checkIfSelfTrade(Connection dbConnection, UUID tradeUUID, String userName) throws SQLException {

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(this.stmtCheckSelfTrade)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeUUID));
            preparedStatement.setString(2, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && resultSet.getInt(1) > 0;

        }

    }

    public List<TradeViewModel> fetchTrades() throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(this.stmtFetchTrades)) {

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

        }

    }

    public void deleteTradeNormalUser(UUID tradeID, String userName) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(this.stmtDeleteTradeNormalUser)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeID));
            preparedStatement.setString(2, userName);

            preparedStatement.execute();

        }

    }

    public void deleteTradeAdmin(UUID tradeID) throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(this.stmtDeleteTradeAdmin)) {

            preparedStatement.setObject(1, DataTransformation.prepareUUID(tradeID));

            preparedStatement.execute();

        }

    }

}
