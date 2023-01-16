package julio.cardGame.cardGameServer.router;


import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.Header;
import julio.cardGame.cardGameServer.http.HeadersValidator;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.common.HttpStatus;

import java.sql.SQLException;
import java.util.List;

public abstract class AuthenticatedRoute {

    protected AuthorizationWrapper requireAuthToken(List<Header> headers) throws SQLException {

        String authToken = HeadersValidator.validateToken(headers);

        if (authToken == null) {

            return new AuthorizationWrapper(null,
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );

        }

        String userName = new UserRepo().checkTokenReturnUser(authToken);

        if (userName == null) {
            return new AuthorizationWrapper(null,
                    new Response(HttpStatus.UNAUTHORIZED.getStatusMessage(), HttpStatus.UNAUTHORIZED)
            );
        }

        return new AuthorizationWrapper(userName, null);

    }

    //todo check in the db if admin
    protected AuthorizationWrapper requireAdmin(List<Header> headers) throws SQLException {

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

    protected boolean canAccessUserData(List<Header> headers, String requestedUser) throws SQLException {

        UserRepo userRepo = new UserRepo();

        String token = HeadersValidator.validateToken(headers);

        if (token == null)
            return false;

        if (userRepo.checkAdmin(token))
            return true;

        return userRepo.checkTokenBelongsToUser(token, requestedUser);


    }

}
