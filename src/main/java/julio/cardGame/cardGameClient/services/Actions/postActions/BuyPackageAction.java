package julio.cardGame.cardGameClient.services.Actions.postActions;

import julio.cardGame.cardGameClient.application.CardGame;
import julio.cardGame.cardGameClient.helpers.requests.GetRequestBuilder;
import julio.cardGame.cardGameClient.helpers.requests.PostRequestBuilder;
import julio.cardGame.cardGameClient.helpers.requests.RequestBuilder;
import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameServer.http.Header;
import julio.cardGame.cardGameServer.http.HttpPath;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class BuyPackageAction implements Action {
    @Override
    public HttpRequest executeAction() {

        RequestBuilder requestBuilder = new PostRequestBuilder();

        //todo auth is checked from the static variable
        List<Header> headerList = new ArrayList<>();
        headerList.add(new Header("authHe", CardGame.authHeader));

        HttpRequest request;

            try {
                request = requestBuilder.buildRequest(
                        headerList,
                        HttpPath.TRANSACTIONS_PACKAGES.getPath(),
                        HttpRequest.BodyPublishers.ofString("")
                );
            } catch (IOException | IllegalArgumentException e) {
                throw new RuntimeException(e);
            }

        return request;
        //return new ServerResponse("Package bought", null, ClientAction.BUY_PACKAGE);
    }
}
