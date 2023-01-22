package julio.cardGame.cardGameServer.http.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.http.HttpServer;
import julio.cardGame.cardGameServer.http.communication.headers.Header;
import julio.cardGame.cardGameServer.http.communication.headers.HeaderParser;
import julio.cardGame.cardGameServer.http.routing.PathParser;
import julio.cardGame.cardGameServer.http.routing.router.RouteIdentifier;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//The class which executes the client request and sends a response to the same socket

public class ClientExecutor implements Runnable {

    private final Socket actualSocket;

    public ClientExecutor(Socket newSocket) {
        this.actualSocket = newSocket;
    }
    @Override
    public void run() {

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader
                            (this.actualSocket.getInputStream()));

            final RequestContext requestContext = this.parseRequest(br);

            //now that we parsed the request into the correct context we can handle it
            Sendable response = this.generateResponse(requestContext);

            //Sendable response;
            BufferedWriter w = new BufferedWriter(
                    new OutputStreamWriter(this.actualSocket.getOutputStream()));

            response.sendResponse(w);
            w.close();
            actualSocket.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private RequestContext parseRequest(BufferedReader bufferedReader) throws IOException {

        RequestContext requestContext = new RequestContext();

        String versionString = bufferedReader.readLine();

        this.parseEndPoint(requestContext, versionString);

        List<Header> headers = new ArrayList<>();
        HeaderParser headerParser = new HeaderParser();

        String input;
        do {

            input = bufferedReader.readLine();
            if ("".equals(input)) {
                break;
            }
            headers.add(headerParser.parseHeader(input));

        } while (true);

        requestContext.setHeaders(headers);

        int contentLength = requestContext.getContentLength();
        char[] buffer = new char[contentLength];
        bufferedReader.read(buffer, 0, contentLength);
        String body = new String(buffer);
        requestContext.setBody(body);

        return requestContext;

    }

    private Sendable generateResponse(RequestContext requestContext) throws IOException {

        try {

            Routeable route = HttpServer.FUNCTIONAL_ROUTER
                    .findRoute(
                            new RouteIdentifier(requestContext.getPath(), requestContext.getHttpVerb())
                    ).generateRoute();

            return route.process(requestContext);


        } catch (JsonProcessingException | NoSuchAlgorithmException | SQLException e) {

            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (NullPointerException e) {

            return new Response(DefaultMessages.ERR_ROUTE_NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }

    public void parseEndPoint(RequestContext requestContext, String versionString) throws IOException {
        final String[] splitVersionString = versionString.split(" ");
        requestContext.setHttpVerb(splitVersionString[0]);

        PathParser.parsePath(requestContext, splitVersionString[1]);
    }

}
