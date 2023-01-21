package julio.cardGame.cardGameServer.services.userServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.models.ScoreModel;
import julio.cardGame.cardGameServer.repositories.ScoreboardRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.sql.SQLException;
import java.util.List;

public class GetScoreService implements CardGameService {

    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        ScoreboardRepo scoreboardRepo = new ScoreboardRepo();

        List<ScoreModel> scoreBoard = scoreboardRepo.fetchScoreBoard();

        if (scoreBoard.size() == 0)
            return new Response(DefaultMessages.SCORE_NO_RESULTS.getMessage(), HttpStatus.OK);

        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(scoreBoard);

        return new Response(body, HttpStatus.OK, true);

    }
}
