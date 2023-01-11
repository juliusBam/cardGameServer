package julio.cardGame.cardGameClient.services.Actions.postActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameClient.helpers.readers.CredentialReader;
import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.requests.PostRequestBuilder;
import julio.cardGame.cardGameClient.helpers.requests.RequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.HttpPath;
import julio.cardGame.cardGameServer.application.serverLogic.models.UserLoginDataModel;

import java.io.IOException;
import java.net.http.HttpRequest;

public class LoginAction implements Action {
    @Override
    public HttpRequest executeAction() throws IOException {

        RequestBuilder requestBuilder = new PostRequestBuilder();

        UserLoginDataModel credentials = new CredentialReader().readCredentials();

        String body = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(credentials);

        HttpRequest request;


        try {
            request = requestBuilder.buildRequest(
                    HeadersBuilder.buildHeaders(true),
                    HttpPath.SESSIONS.getPath(),
                    HttpRequest.BodyPublishers.ofString(body)
            );
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return request;

    }
}
