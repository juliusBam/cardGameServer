package julio.cardGame.cardGameServer.router;

import julio.cardGame.cardGameServer.http.Response;

public class AuthorizationWrapper {
    public Response response;
    public String userName;

    public AuthorizationWrapper(String userName, Response response) {
        this.response = response;
        this.userName = userName;
    }
}
