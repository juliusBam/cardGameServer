package julio.cardGame.cardGameServer.router.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.DefaultMessages;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.RequestParameters;
import julio.cardGame.cardGameServer.application.dbLogic.models.CardDeckModel;

import java.sql.SQLException;
import java.util.List;

public class ExecuteGetDeck extends AuthenticatedRoute implements Routeable {
    @Override
    public Response process(RequestContext requestContext) {

        try {

            UserRepo userRepo = new UserRepo();

            AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

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

            } else {

                body = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(deckObj);

            }

            return new Response(body, HttpStatus.OK);

        } catch (SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_DECK.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }



}
