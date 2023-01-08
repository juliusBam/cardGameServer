package julio.cardGame.cardGameClient;

import julio.cardGame.cardGameClient.application.CardGame;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static julio.cardGame.common.Constants.HOST_ADDR;
import static julio.cardGame.common.Constants.LISTENING_PORT_STR;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to the card game client");


        /*try {

            HttpClient client = HttpClient.newBuilder().build();

            String response = client.send(
                    HttpRequest
                            .newBuilder()
                            .GET()
                            .setHeader("", "")
                            .setHeader("", "")
                            .uri(URI.create("http://localhost:10001/cards"))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            ).body();

            System.out.println(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

        try {

            CardGame game = CardGame.getInstance();

            try {
                game.startClient();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } catch (UncheckedIOException e) {
            System.err.println("An error occurred while starting the client:");
            System.err.println(e.getMessage());
        }




    }
}