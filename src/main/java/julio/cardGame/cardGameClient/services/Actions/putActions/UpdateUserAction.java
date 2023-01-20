package julio.cardGame.cardGameClient.services.Actions.putActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameClient.application.CardGame;
import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.readers.GenericStringReader;
import julio.cardGame.cardGameClient.helpers.requests.PutRequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.routing.HttpPath;
import julio.cardGame.cardGameServer.models.UserAdditionalDataModel;

import javax.security.auth.login.CredentialException;
import java.io.IOException;
import java.net.http.HttpRequest;

public class UpdateUserAction implements Action {
    @Override
    public HttpRequest executeAction() throws IOException, CredentialException {

        //todo delete
        CardGame.setLoggedUser("Julio");
        CardGame.authHeader = "djasklds";

        if (CardGame.authHeader.isEmpty() || CardGame.authHeader.isBlank()
            || CardGame.getLoggedUser().isEmpty() || CardGame.getLoggedUser().isBlank())
            throw new CredentialException("Invalid credential");

        GenericStringReader stringReader = new GenericStringReader();

        System.out.println("========== Name ==========");
        String name = stringReader.readString();

        System.out.println("========== Bio ==========");
        String bio = stringReader.readString();

        System.out.println("========== Image ==========");
        String img = stringReader.readString();

        UserAdditionalDataModel userData = new UserAdditionalDataModel(name, bio, img);

        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(userData);

        String endpoint = HttpPath.USERS.getPath() + "/" + CardGame.getLoggedUser();

        HttpRequest request;

        try {

            request = new PutRequestBuilder()
                    .buildRequest(
                            HeadersBuilder.buildHeaders(true),
                            endpoint,
                            HttpRequest.BodyPublishers.ofString(body)
                    );

        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return request;
    }
}
