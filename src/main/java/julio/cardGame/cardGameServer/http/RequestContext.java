package julio.cardGame.cardGameServer.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestContext extends HttpMessage {

    private static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";
    private String httpVerb;
    private String path;

    private List<Header> headers;

    private final Map<String, String> parameters = new HashMap<>();

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    public String fetchParameter(String key) {
        return parameters.get(key);
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    public void setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        //todo add path parser for the parameters/user names
        this.path = path;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    public int getContentLenght() {

        return headers.stream()
                .filter(header -> CONTENT_LENGTH_HEADER_NAME.equals(header.getName()))
                .findFirst()
                .map(Header::getValue)
                .map(Integer::parseInt)
                .orElse(0);

    }

    public void print() {
        System.out.println(this.httpVerb);
        System.out.println(this.headers);
        System.out.println(this.body);
    }
}
