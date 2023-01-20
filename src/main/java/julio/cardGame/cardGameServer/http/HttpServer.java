package julio.cardGame.cardGameServer.http;

import julio.cardGame.cardGameServer.battle.BattleWrapper;
import julio.cardGame.cardGameServer.http.communication.*;
import julio.cardGame.cardGameServer.http.routing.router.FunctionalRouter;
import julio.cardGame.cardGameServer.Constants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    public final static FunctionalRouter FUNCTIONAL_ROUTER = new FunctionalRouter();

    public static BattleWrapper battleWrapper = new BattleWrapper();

    public final ExecutorService executorService = Executors.newFixedThreadPool(10);


    public void start() {

        try (ServerSocket listener = new ServerSocket(Constants.LISTENING_PORT)) {
            //ExecutorService executorService = Executors.newFixedThreadPool(10);
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
