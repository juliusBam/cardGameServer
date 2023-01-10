package julio.cardGame.cardGameServer.router;


import julio.cardGame.cardGameServer.http.Header;
import julio.cardGame.cardGameServer.http.HeadersValidator;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.HttpStatus;

import java.util.List;

public abstract class AuthenticatedRoute {

    protected AuthorizationWrapper requireAuthToken(List<Header> headers) {

        String authToken = HeadersValidator.validateToken(headers);

        if (authToken == null) {
            return new AuthorizationWrapper(null,
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );

        }

        String userName = HeadersValidator.extractUserName(authToken);

        if (userName == null) {
            return new AuthorizationWrapper(null,
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );
        }

        return new AuthorizationWrapper(userName, null);

    }

    protected AuthorizationWrapper requireAdmin(List<Header> headers) {

        AuthorizationWrapper authorizationWrapper = this.requireAuthToken(headers);

        if (authorizationWrapper.response != null) {
            return authorizationWrapper;
        }

        if (authorizationWrapper.userName.contains("admin")) {
            return authorizationWrapper;
        } else {
            return new AuthorizationWrapper(authorizationWrapper.userName,
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );
        }

    }

}
