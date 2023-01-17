package julio.cardGame.cardGameServer.http.routing.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestParameters;
import julio.cardGame.cardGameServer.database.models.CardDeckModel;

import java.sql.SQLException;
import java.util.List;

public class ExecuteGetDeck extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            UserRepo userRepo = new UserRepo();

            List<CardDeckModel> deckObj = userRepo.fetchDeckCards(auth.userName);

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



        } catch (SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_DECK.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }



}
