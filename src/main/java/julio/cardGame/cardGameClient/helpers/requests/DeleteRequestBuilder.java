package julio.cardGame.cardGameClient.helpers.requests;

import julio.cardGame.cardGameServer.http.communication.headers.Header;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;

public class DeleteRequestBuilder extends RequestBuilder {

    @Override
    public HttpRequest buildRequest(List<Header> newHeaders, String endpoint, HttpRequest.BodyPublisher bodyPublisher) throws IOException {

        List<String> headers = this.buildHeaders(newHeaders);

        //if no headers
        if (headers == null || headers.size() == 0) {
            return HttpRequest
                    .newBuilder()
                    .DELETE()
                    .uri(this.buildURI(endpoint))
                    .build();
        }

        return HttpRequest
                .newBuilder()
                .DELETE()
                .headers(headers.toArray(String[]::new))
                .uri(this.buildURI(endpoint))
                .build();
    }
}
