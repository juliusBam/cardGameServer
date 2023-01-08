package julio.cardGame.cardGameServer.http;

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

    public static boolean checkAdmin(String authToken) {

        return authToken.contains("admin");

    }

    public static String extractUserName(String authToken) {
        return authToken.substring(authToken.indexOf(" ") + 1, authToken.indexOf("-"));
    }
}
