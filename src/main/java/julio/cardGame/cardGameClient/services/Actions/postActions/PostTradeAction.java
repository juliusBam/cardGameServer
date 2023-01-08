package julio.cardGame.cardGameClient.services.Actions.postActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.readers.TradeDealReader;
import julio.cardGame.cardGameClient.helpers.requests.PostRequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.HttpPath;
import julio.cardGame.common.models.TradeModel;

import java.io.IOException;
import java.net.http.HttpRequest;

public class PostTradeAction implements Action {
    @Override
    public HttpRequest executeAction() throws IOException {

        TradeModel tradeDeal = new TradeDealReader().readTradeDeal();

        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(tradeDeal);

        HttpRequest request;

        try {
            request = new PostRequestBuilder()
                        .buildRequest(
                            HeadersBuilder.buildHeaders(true),
                            HttpPath.TRADINGS.getPath(),
                            HttpRequest.BodyPublishers.ofString(body)
            );
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return request;
    }
}
