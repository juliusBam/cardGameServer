package julio.cardGame.cardGameServer.http.communication.headers;

import java.util.List;
import java.util.Optional;

public class HeadersValidator {
    public static String validateToken(List<Header> headers) {

        Optional<Header> authHeader = headers.stream().filter(
                header -> header.getName().equals("Authorization")
        ).findAny();

        if (authHeader.isEmpty())
            return null;


        String token = authHeader.get().getValue();

        if (!token.contains("Bearer") || !token.contains("-mtcgToken"))
            return null;

        return token;

    }

}
