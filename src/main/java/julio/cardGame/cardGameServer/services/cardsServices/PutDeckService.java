package julio.cardGame.cardGameServer.services.cardsServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.database.db.DbConnection;
import julio.cardGame.cardGameServer.database.repositories.CardRepo;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import javax.naming.AuthenticationException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PutDeckService implements CardGameService {
    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        List<UUID> cardIds = new ObjectMapper()
                .readValue(requestContext.getBody(), new TypeReference<List<UUID>>() {
                });

        //only 4 cards allowed
        if (cardIds.size() != Constants.DECK_SIZE)
            return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);

        try (Connection dbConn = DbConnection.getInstance().connect()) {

            try {

                //once the cardids are parsed we can execute the sqls
                UserRepo userRepo = new UserRepo();

                boolean hasDeck = userRepo.checkIfDeck(dbConn, authorizationWrapper.userName);

                //user already has a deck
                if (hasDeck) {
                    //dbConn.close();
                    return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
                }

                //we check if all the cards belong to the user
                int ownedCards = userRepo.checkCardsOwnership(dbConn, cardIds, authorizationWrapper.userName);

                if (ownedCards != Constants.DECK_SIZE) {
                    return new Response(HttpStatus.BAD_REQUEST.getStatusMessage(), HttpStatus.BAD_REQUEST);
                }

                CardRepo cardRepo = new CardRepo();

                if (cardRepo.checkIfCardInDeck(cardIds) > 0)
                    return new Response(DefaultMessages.ERR_CARD_IN_TRADE.getMessage(), HttpStatus.BAD_REQUEST);

                UUID newDeckID = UUID.randomUUID();

                dbConn.setAutoCommit(false);

                userRepo.addDeckID(dbConn, newDeckID, authorizationWrapper.userName);

                cardRepo.moveCardsToDeck(dbConn, newDeckID, cardIds);

                dbConn.commit();

                return new Response(HttpStatus.OK.getStatusMessage(), HttpStatus.OK);

            } catch (SQLException e) {

                //if we started the transaction we rollback
                if (!dbConn.getAutoCommit()) {
                    dbConn.rollback();
                }
                return new Response(e);

            } catch (AuthenticationException e) {

                //dbConn.close();
                return new Response(e.getMessage(), HttpStatus.UNAUTHORIZED);

            }
        }
    }
}
