package julio.cardGame.cardGameServer.http.communication;

import julio.cardGame.cardGameServer.http.communication.headers.Header;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Response extends HttpMessage implements Sendable {

    private HttpStatus httpStatus;

    private final List<Header> additionalHeaders = new ArrayList<>();

    public Response() {
        super();
    }

    public Response(String body, HttpStatus status) {
        this.body = body;
        this.httpStatus = status;
    }

    public Response(String body, HttpStatus status, boolean isJson) {
        this.body = body;
        this.httpStatus = status;

        if(isJson)
            this.addJsonContentHeader();
    }

    private void addJsonContentHeader() {
        this.additionalHeaders.add(
                new Header(
                        HeaderTypes.CONTENT_TYPE.getHeaderValue(),
                        "application/json"
                )
        );
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

        int contentLength = body.length();

        this.additionalHeaders.add(
                new Header(
                        HeaderTypes.CONTENT_LENGTH.getHeaderValue(),
                        Integer.toString(contentLength)
                )
        );

        String statusLine = String.format("HTTP/1.1 %s %s",
                httpStatus.getStatusCode(),
                httpStatus.getStatusMessage());

        bufferedWriter.write(statusLine);
        bufferedWriter.newLine();

        if (!additionalHeaders.isEmpty()) {

            for (Header header : this.additionalHeaders) {

                bufferedWriter.write(header.toString());
                bufferedWriter.newLine();

            }

        }


        bufferedWriter.newLine();
        bufferedWriter.write(body);
        bufferedWriter.newLine();
        bufferedWriter.newLine();

        bufferedWriter.flush();
    }

}
