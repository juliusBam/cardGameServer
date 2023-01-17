package julio.cardGame.cardGameClient.services.Actions.getActions;

import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.readers.UsernameReader;
import julio.cardGame.cardGameClient.helpers.requests.GetRequestBuilder;
import julio.cardGame.cardGameClient.helpers.requests.RequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.routing.HttpPath;

import java.io.IOException;
import java.net.http.HttpRequest;

public class ShowUserAction implements Action {
    @Override
    public HttpRequest executeAction() throws IOException {
        RequestBuilder requestBuilder = new GetRequestBuilder();

        String userName = new UsernameReader().readUsername();

        String endpoint = HttpPath.USERS.getPath() + "/" + userName;

        HttpRequest request;

        try {

            request = requestBuilder.buildRequest(
                    HeadersBuilder.buildHeaders(false),
                    endpoint,
                    null
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return request;
    }
}
