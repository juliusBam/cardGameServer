package julio.cardGame.cardGameServer.services.tradingServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.models.TradeViewModel;
import julio.cardGame.cardGameServer.repositories.TradeRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.sql.SQLException;
import java.util.List;

public class GetTradingService implements CardGameService {
    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException {

        TradeRepo tradeRepo = new TradeRepo();

        List<TradeViewModel> trades = tradeRepo.fetchTrades();

        if (trades.size() == 0) {
            return new Response(DefaultMessages.NO_TRADES.getMessage(), HttpStatus.OK);
        }

        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(trades);

        return new Response(body, HttpStatus.OK, true);

    }
}
