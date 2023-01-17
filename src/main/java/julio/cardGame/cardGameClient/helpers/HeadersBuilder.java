package julio.cardGame.cardGameClient.helpers;

import julio.cardGame.cardGameClient.application.CardGame;
import julio.cardGame.cardGameServer.http.communication.headers.Header;

import java.util.ArrayList;
import java.util.List;

public class HeadersBuilder {
    public static List<Header> buildHeaders(boolean json) {

        List<Header> headers = new ArrayList<>();

        if (CardGame.authHeader != null && !CardGame.authHeader.isEmpty() && !CardGame.authHeader.isBlank())
            headers.add(new Header("Authorization", CardGame.authHeader));

        if (json)
            headers.add(new Header("Content-Type", "application/json"));

        return headers;
    }
}
