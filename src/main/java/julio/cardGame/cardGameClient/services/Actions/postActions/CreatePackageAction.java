package julio.cardGame.cardGameClient.services.Actions.postActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.readers.PackageReader;
import julio.cardGame.cardGameClient.helpers.requests.PostRequestBuilder;
import julio.cardGame.cardGameClient.helpers.requests.RequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.HttpPath;
import julio.cardGame.cardGameServer.application.dbLogic.models.CardRequestModel;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;

public class CreatePackageAction implements Action {
    @Override
    public HttpRequest executeAction() throws IOException {

        List<CardRequestModel> cardPackage = new PackageReader().readPackage();

        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(cardPackage);

        RequestBuilder requestBuilder = new PostRequestBuilder();

        HttpRequest request;

        try {
            request = requestBuilder.buildRequest(
                    HeadersBuilder.buildHeaders(true),
                    HttpPath.PACKAGES.getPath(),
                    HttpRequest.BodyPublishers.ofString(body)
            );
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return request;

    }
}
