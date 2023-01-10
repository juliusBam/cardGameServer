package julio.cardGame.cardGameServer.http;

import julio.cardGame.cardGameServer.application.serverLogic.BattleResultObs;
import julio.cardGame.common.Constants;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.cardGameServer.router.RouteIdentifier;
import julio.cardGame.cardGameServer.router.Router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private final Router router = new Router();

    public static BattleResultObs battleRes = new BattleResultObs();

    public void start() {

        try (ServerSocket listener = new ServerSocket(Constants.LISTENING_PORT)) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            while (true) {
                try {
                    final Socket socket = listener.accept();
                    executorService.submit(new ClientExecutor(socket));
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

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
                /*
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    requestContext.print();
                */

                //now that we parsed the request into the correct context we can handle it
                Routeable routeable = router.findRoute(new RouteIdentifier(requestContext.getPath(), requestContext.getHttpVerb()));

                Sendable response;

                if (routeable != null) {
                    response = routeable.process(requestContext);
                } else {
                    response = new Response(true);
                }

                BufferedWriter w = new BufferedWriter(
                        new OutputStreamWriter(this.actualSocket.getOutputStream()));

                response.sendResponse(w);
                w.close();
                actualSocket.close();

            } catch (IOException | NoSuchAlgorithmException e) {
                System.err.println(e.getMessage());
            }
        }

        public RequestContext parseRequest(BufferedReader bufferedReader) throws IOException {

            RequestContext requestContext = new RequestContext();

            String versionString = bufferedReader.readLine();
            final String[] splitVersionString = versionString.split(" ");
            requestContext.setHttpVerb(splitVersionString[0]);

            PathParser.parsePath(requestContext, splitVersionString[1]);
            //requestContext.setPath(splitVersionString[1]);

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

            int contentLength = requestContext.getContentLenght();
            char[] buffer = new char[contentLength];
            bufferedReader.read(buffer, 0, contentLength);
            String body = new String(buffer);
            requestContext.setBody(body);

            return requestContext;
        }
    }

}
