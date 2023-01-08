package julio.cardGame.cardGameClient.helpers.requests;

import julio.cardGame.cardGameClient.application.CardGame;
import julio.cardGame.cardGameServer.http.Header;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import static julio.cardGame.common.Constants.HOST_ADDR;
import static julio.cardGame.common.Constants.LISTENING_PORT_STR;

public abstract class RequestBuilder {

    public abstract HttpRequest buildRequest(List<Header> newHeaders,
                                             String endpoint,
                                             HttpRequest.BodyPublisher bodyPublisher)
            throws IOException, IllegalArgumentException;

    protected List<String> buildHeaders(List<Header> newHeaders) {
        List<String> headers = new ArrayList<>();

        if (!CardGame.authHeader.isEmpty() && !CardGame.authHeader.isBlank()) {
            headers.add("Authorization");
            headers.add(CardGame.authHeader);
        }

        if (newHeaders != null && !newHeaders.isEmpty()) {
            //now we iterate over the remaining headers and add them to the generated
            for (Header h : newHeaders) {
                headers.add(h.getName());
                headers.add(h.getValue());
            }
        }

        return headers;
    }

    protected URI buildURI(String endpoint) {

        URI uri;

        if (endpoint != null && !endpoint.isBlank() && !endpoint.isEmpty()) {
            uri = URI.create(HOST_ADDR + LISTENING_PORT_STR + endpoint);
        } else {
            uri = URI.create(HOST_ADDR + LISTENING_PORT_STR);
        }

        return uri;
    }
}
