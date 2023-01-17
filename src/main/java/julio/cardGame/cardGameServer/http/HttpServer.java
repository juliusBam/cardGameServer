package julio.cardGame.cardGameServer.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.battle.BattleWrapper;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.communication.headers.Header;
import julio.cardGame.cardGameServer.http.communication.headers.HeaderParser;
import julio.cardGame.cardGameServer.http.routing.PathParser;
import julio.cardGame.cardGameServer.http.routing.router.FunctionalRouter;
import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.http.routing.router.RouteEntry;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.routing.router.RouteIdentifier;
import julio.cardGame.cardGameServer.http.routing.router.Router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    //private final Router router = new Router();

    public final static FunctionalRouter functionalRouter = new FunctionalRouter();

    //public static BattleExecutor battleRes = new BattleExecutor();

    public static BattleWrapper battleWrapper = new BattleWrapper();


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

}
