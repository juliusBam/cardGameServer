package julio.cardGame.cardGameClient.services.Actions.getActions;

import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.requests.GetRequestBuilder;
import julio.cardGame.cardGameClient.helpers.requests.RequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.routing.HttpPath;

import java.io.IOException;
import java.net.http.HttpRequest;

public class FetchCardsAction implements Action {
    @Override
    public HttpRequest executeAction() {

        RequestBuilder requestBuilder = new GetRequestBuilder();

        HttpRequest request;

        try {
            request = requestBuilder.buildRequest(
                    HeadersBuilder.buildHeaders(false),
                    HttpPath.CARDS.getPath(),
                    null
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return request;

    }
}
