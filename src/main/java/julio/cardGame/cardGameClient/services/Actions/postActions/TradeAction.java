package julio.cardGame.cardGameClient.services.Actions.postActions;

import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.readers.UUIDreader;
import julio.cardGame.cardGameClient.helpers.requests.PostRequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.HttpPath;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.UUID;

public class TradeAction implements Action {
    @Override
    public HttpRequest executeAction() throws IOException {

        System.out.println("========= Trade ID =========");
        UUID tradeId = new UUIDreader().readUUID();

        System.out.println("========= Card to trade ID =========");
        UUID cardId = new UUIDreader().readUUID();

        String body = cardId.toString();

        String endpoint = HttpPath.TRADINGS.getPath() + "/" + tradeId.toString();

        HttpRequest request;

        try {

            request = new PostRequestBuilder()
                    .buildRequest(
                            HeadersBuilder.buildHeaders(false),
                            endpoint,
                            HttpRequest.BodyPublishers.ofString(body)
                    );

        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return request;

    }
}
