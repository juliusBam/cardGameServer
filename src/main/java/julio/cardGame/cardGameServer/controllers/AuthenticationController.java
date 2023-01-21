package julio.cardGame.cardGameServer.controllers;

import julio.cardGame.cardGameServer.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.communication.headers.Header;
import julio.cardGame.cardGameServer.http.communication.headers.HeadersValidator;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;

import java.sql.SQLException;
import java.util.List;

public class AuthenticationController {

    public static AuthorizationWrapper requireAuthToken(List<Header> headers) throws SQLException {

        String authToken = HeadersValidator.validateToken(headers);

        if (authToken == null) {

            return new AuthorizationWrapper(
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );

        }

        String userName = new UserRepo().checkTokenReturnUser(authToken);

        if (userName == null) {
            return new AuthorizationWrapper(
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );
        }

        return new AuthorizationWrapper(userName, authToken, false);

    }
    public static AuthorizationWrapper requireAdmin(List<Header> headers) throws SQLException {

        AuthorizationWrapper authorizationWrapper = AuthenticationController.requireAuthToken(headers);

        //if token invalid return wrapper with err response
        if (authorizationWrapper.response != null) {
            return authorizationWrapper;
        }

        String token = HeadersValidator.validateToken(headers);

        if (token == null) {

            return
                    new AuthorizationWrapper(
                            new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
                    );

        }


        UserRepo userRepo = new UserRepo();

        if (!userRepo.checkAdminByToken(token)) {
            return new AuthorizationWrapper(
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );
        }

        return new AuthorizationWrapper(
                userRepo.checkTokenReturnUser(token),
                token,
                true
        );

    }

    public static AuthorizationWrapper canAccessUserData(List<Header> headers, String requestedUser) throws SQLException {

        AuthorizationWrapper authorizationWrapper = AuthenticationController.requireAuthToken(headers);

        if (authorizationWrapper.response != null) {

            return authorizationWrapper;

        }

        if (authorizationWrapper.token == null) {

            return new AuthorizationWrapper(
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );

        }

        UserRepo userRepo = new UserRepo();

        if (userRepo.checkAdminByToken(authorizationWrapper.token)) {

            return new AuthorizationWrapper(
                    authorizationWrapper.userName,
                    authorizationWrapper.token,
                    true
            );

        }

        if (userRepo.checkTokenBelongsToUser(authorizationWrapper.token, requestedUser)) {

            return new AuthorizationWrapper(
                    authorizationWrapper.userName,
                    authorizationWrapper.token,
                    false
            );

        } else {

            return new AuthorizationWrapper(
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );

        }

    }

}
