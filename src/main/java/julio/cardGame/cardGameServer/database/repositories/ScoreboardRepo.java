package julio.cardGame.cardGameServer.database.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.db.DataTransformation;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.models.ScoreModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardRepo {

    public List<ScoreModel> fetchScoreBoard() throws SQLException {

        String sql = """
                    SELECT
                        t.*
                    FROM public."scoreBoard" t;
                """;

        try (PreparedStatement preparedStatement = DbConnection.getInstance().prepareStatement(sql)) {

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
