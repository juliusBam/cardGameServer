package julio.cardGame.cardGameServer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.models.CardDbModel;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;

import java.sql.SQLException;
import java.util.List;

public class GetCardService implements CardGameService {

    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        CardRepo cardRepo = new CardRepo();

        List<CardDbModel> cards = cardRepo.fetchUserCards(authorizationWrapper.userName);

        if (cards.size() == 0)
            return new Response(DefaultMessages.USER_NO_CARDS.getMessage(), HttpStatus.OK);

        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(cards);

        return new Response(body, HttpStatus.OK, true);

    }
}
