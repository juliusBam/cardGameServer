package julio.cardGame.cardGameServer.http.routing.routes.get;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.models.TradeViewModel;
import julio.cardGame.cardGameServer.database.repositories.TradeRepo;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;

import java.sql.SQLException;
import java.util.List;

public class ExecuteGetTrading implements Routeable {

    private final TradeRepo tradeRepo;

    public ExecuteGetTrading() {
        this.tradeRepo = new TradeRepo();
    }

    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = AuthenticationController.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            List<TradeViewModel> trades = tradeRepo.fetchTrades();

            if (trades.size() == 0) {
                return new Response(DefaultMessages.NO_TRADES.getMessage(), HttpStatus.OK);
            }

            String body = new ObjectMapper()
                      .writerWithDefaultPrettyPrinter()
                      .writeValueAsString(trades);

            return new Response(body, HttpStatus.OK, true);


        } catch (SQLException e) {

            return new Response(e);

        } catch (JsonProcessingException e) {

            return new Response(DefaultMessages.ERR_JSON_PARSE_TRADE.getMessage(), e);

        }

    }

}
