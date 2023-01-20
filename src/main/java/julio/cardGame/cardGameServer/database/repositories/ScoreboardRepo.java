package julio.cardGame.cardGameServer.database.repositories;

import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.models.ScoreModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardRepo {

    private final String stmtFetchScoreBoard = """
                    SELECT t.*
                        FROM public."scoreBoard" t;
                """;

    public List<ScoreModel> fetchScoreBoard() throws SQLException {

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(stmtFetchScoreBoard)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<ScoreModel> scoreBoard = new ArrayList<>();

            while (resultSet.next()) {

                int wins = resultSet.getInt(3);
                int losses = resultSet.getInt(4);

                scoreBoard.add(
                        new ScoreModel(
                                wins,
                                losses,
                                DataTransformation.calculateWinRate(wins, losses),
                                resultSet.getInt(2),
                                resultSet.getString(1)
                        )
                );
            }

            if (scoreBoard.size() == 0) {
                return null;
            }

            return scoreBoard;

        } catch (SQLException e) {
            throw e;
        }

    }

}
