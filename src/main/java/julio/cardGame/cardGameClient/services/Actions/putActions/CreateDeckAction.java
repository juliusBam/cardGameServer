package julio.cardGame.cardGameClient.services.Actions.putActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import julio.cardGame.cardGameClient.helpers.HeadersBuilder;
import julio.cardGame.cardGameClient.helpers.readers.DeckReader;
import julio.cardGame.cardGameClient.helpers.requests.PutRequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.HttpPath;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;

public class CreateDeckAction implements Action {
    @Override
    public HttpRequest executeAction() throws IOException {

        List<String> deck = new DeckReader().readDeck();

        String body = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(deck);

        HttpRequest request;

        try {

            request = new PutRequestBuilder()
                    .buildRequest(
                            HeadersBuilder.buildHeaders(true),
                            HttpPath.DECK.getPath(),
                            HttpRequest.BodyPublishers.ofString(body)
                    );

        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return request;
    }
}
