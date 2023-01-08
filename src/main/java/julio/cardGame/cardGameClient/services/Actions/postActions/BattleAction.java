package julio.cardGame.cardGameClient.services.Actions.postActions;

import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.requests.PostRequestBuilder;
import julio.cardGame.cardGameClient.helpers.requests.RequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.HttpPath;

import java.io.IOException;
import java.net.http.HttpRequest;

public class BattleAction implements Action {
    @Override
    public HttpRequest executeAction() {

        RequestBuilder requestBuilder = new PostRequestBuilder();

        HttpRequest request;

        try {

            request = requestBuilder.buildRequest(
                    HeadersBuilder.buildHeaders(false),
                    HttpPath.BATTLES.getPath(),
                    null
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return request;

    }
}
