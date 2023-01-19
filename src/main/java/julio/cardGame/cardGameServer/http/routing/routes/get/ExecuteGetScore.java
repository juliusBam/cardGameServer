package julio.cardGame.cardGameServer.http.routing.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.models.ScoreModel;
import julio.cardGame.cardGameServer.database.repositories.ScoreboardRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import java.sql.SQLException;
import java.util.List;

public class ExecuteGetScore implements Routeable {

    private final ScoreboardRepo scoreboardRepo;
    public ExecuteGetScore() {
        this.scoreboardRepo = new ScoreboardRepo();
    }

    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = AuthenticationController.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            List<ScoreModel> scoreBoard = this.scoreboardRepo.fetchScoreBoard();

            if (scoreBoard.size() == 0)
                return new Response(DefaultMessages.SCORE_NO_RESULTS.getMessage(), HttpStatus.OK);

            String body = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(scoreBoard);

            return new Response(body, HttpStatus.OK, true);

        } catch (SQLException e) {

            return new Response(e);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_SCOREBOARD.getMessage(), e);

        }

    }

}
