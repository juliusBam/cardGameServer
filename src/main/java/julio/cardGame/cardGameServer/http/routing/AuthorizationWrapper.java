package julio.cardGame.cardGameServer.http.routing;

import julio.cardGame.cardGameServer.http.communication.Response;

public class AuthorizationWrapper {
    //when response != null it will be sent, used only for errors
    public Response response;
    public String userName;
    public String token;
    public boolean isAdmin;

    public AuthorizationWrapper(Response newResponse) {

        this.response = newResponse;
        this.userName = null;
        this.token = null;
        this.isAdmin = false;

    }

    public AuthorizationWrapper(String userName, String token, boolean isAdmin) {

        this.response = null;
        this.userName = userName;
        this.token = token;
        this.isAdmin = isAdmin;

    }
}
