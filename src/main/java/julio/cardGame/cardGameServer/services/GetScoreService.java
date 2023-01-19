package julio.cardGame.cardGameServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.models.ScoreModel;
import julio.cardGame.cardGameServer.database.repositories.ScoreboardRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;

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
