package julio.cardGame.cardGameServer.http;

import julio.cardGame.common.HttpStatus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class Response extends HttpMessage implements Sendable {

    private HttpStatus httpStatus;

    public Response() {
        super();
    }

    public Response(String body, HttpStatus status) {
        this.body = body;
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public void sendResponse(BufferedWriter bufferedWriter) throws IOException {
        String statusLine = String.format("HTTP/1.1 %s %s",
                httpStatus.getStatusCode(),
                httpStatus.getStatusMessage());

        bufferedWriter.write(statusLine);
        bufferedWriter.newLine();
        bufferedWriter.newLine();
        bufferedWriter.write(body);
        bufferedWriter.newLine();

        bufferedWriter.flush();
    }

    public Response(boolean error) {
            this.body = "An error occourred";
            this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
