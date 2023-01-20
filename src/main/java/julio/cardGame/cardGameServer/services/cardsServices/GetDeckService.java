package julio.cardGame.cardGameServer.services.cardsServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.models.CardDeckModel;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.sql.SQLException;
import java.util.List;

public class GetDeckService implements CardGameService {

    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        UserRepo userRepo = new UserRepo();

        List<CardDeckModel> deckObj = userRepo.fetchDeckCards(authorizationWrapper.userName);

        if (deckObj.size() == 0)
            return new Response(DefaultMessages.USER_NO_DECK.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        String body = "";

        if (requestContext.fetchParameter(RequestParameters.FORMAT.getParamValue()) != null) {

            for (CardDeckModel card : deckObj) {
                body += card.cardName + ", ";
                body += card.cardType + ", ";
                body += card.card_damage + ", ";
                body += card.cardElement + ", ";
                body += card.monsterRace + "\n";
            }

            return new Response(body, HttpStatus.OK);

        } else {

            body = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(deckObj);

            return new Response(body, HttpStatus.OK, true);
        }

    }

}
