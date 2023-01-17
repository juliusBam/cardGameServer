package julio.cardGame.cardGameClient.helpers.requests;

import julio.cardGame.cardGameServer.http.communication.headers.Header;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;

public class PostRequestBuilder extends RequestBuilder {
    @Override
    public HttpRequest buildRequest(List<Header> newHeaders, String endpoint, HttpRequest.BodyPublisher bodyPublisher) throws IOException {

        if (bodyPublisher == null)
            throw new IllegalArgumentException("Body publisher is null");

        List<String> headers = this.buildHeaders(newHeaders);

        //if no headers
        if (headers == null || headers.size() == 0) {

            return HttpRequest
                    .newBuilder()
                    .POST(bodyPublisher)
                    .uri(this.buildURI(endpoint))
                    .build();

        }

        return HttpRequest
                .newBuilder()
                .POST(bodyPublisher)
                .headers(headers.toArray(String[]::new))
                .uri(this.buildURI(endpoint))
                .build();

    }
}
