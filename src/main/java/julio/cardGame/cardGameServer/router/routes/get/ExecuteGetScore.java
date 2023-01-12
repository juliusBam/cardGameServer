package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.ScoreboardRepo;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import java.sql.SQLException;

public class ExecuteGetScore implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            String body = new ScoreboardRepo().fetchScoreBoard();

            if (body == null)
                return new Response(DefaultMessages.SCORE_NO_RESULTS.getMessage(), HttpStatus.OK);

            return new Response(body, HttpStatus.OK);

        } catch (SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_SCOREBOARD.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    /*private String fetchScoreBoard(Connection dbConnection) throws SQLException, JsonProcessingException {

        String sql = """
                    SELECT
                        t.*
                    FROM public."scoreBoard" t;
                """;

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {

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

            try {

                return new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(scoreBoard);

            } catch (JsonProcessingException e) {
                throw e;
            }

        } catch (SQLException e) {
            throw e;
        }

    }*/
}
