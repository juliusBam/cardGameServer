package julio.cardGame.cardGameClient.services.Actions.deleteActions;

import julio.cardGame.cardGameClient.application.CardGame;
import julio.cardGame.cardGameClient.helpers.readers.UUIDreader;
import julio.cardGame.cardGameClient.helpers.requests.DeleteRequestBuilder;
import julio.cardGame.cardGameClient.helpers.requests.RequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.Header;
import julio.cardGame.cardGameServer.http.HttpPath;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeleteTradeAction implements Action {
    @Override
    public HttpRequest executeAction() throws IOException {

        RequestBuilder requestBuilder = new DeleteRequestBuilder();

        UUID uuid = new UUIDreader().readUUID();

        HttpRequest request;

        String endpoint = HttpPath.TRADINGS.getPath() + "/" + uuid.toString();

        List<Header> headerList = new ArrayList<>();
        headerList.add(new Header("authHe", CardGame.authHeader));

        try {
            request = requestBuilder.buildRequest(
                    headerList,
                    //HeadersBuilder.buildHeaders(false),
                    endpoint,
                    null
            );
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return request;

    }
}
