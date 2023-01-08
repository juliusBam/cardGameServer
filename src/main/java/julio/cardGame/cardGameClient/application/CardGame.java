package julio.cardGame.cardGameClient.application;

import julio.cardGame.cardGameClient.services.Actions.Action;
import julio.cardGame.cardGameClient.helpers.ClientInputParser;
import julio.cardGame.cardGameClient.services.Actions.QuitAction;

import javax.security.auth.login.CredentialException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class CardGame {
    private static CardGame gameInstance;

    //we store the parser in the reader in the class itself, since the game always needs them
    private final ClientInputParser clientInputParser;

    private final BufferedReader inputReader;

    public static String authHeader = "";

    public static HttpClient httpClient;

    private static String loggedUser;

    private CardGame() throws UncheckedIOException {
        this.clientInputParser = new ClientInputParser();
        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
        httpClient = HttpClient.newBuilder().build();
    }
    public static CardGame getInstance() throws UncheckedIOException {
        if (CardGame.gameInstance == null) {
            gameInstance = new CardGame();
        }
        return gameInstance;
    }

    public static String getLoggedUser() {
        return CardGame.loggedUser;
    }

    public static void setLoggedUser(String newUser) {
        CardGame.loggedUser = newUser;
    }

    public void startClient() throws IOException {

        do {

            System.out.printf(">> ");
            String actualInput = this.inputReader.readLine();

            Action requestedAction = this.clientInputParser.parseInput(actualInput);

            if (requestedAction != null) {
                if (requestedAction.getClass().equals(QuitAction.class)) {

                    System.out.println("Quitting card game");
                    break;

                } else {

                    try {
                        String response = httpClient.send(
                                        requestedAction.executeAction(),
                                        HttpResponse.BodyHandlers.ofString())
                                .body();

                        System.out.println(response);

                    } catch (InterruptedException e) {
                        System.err.println("An error occurred during the request");
                        System.err.println(e.getMessage());
                    } catch (CredentialException e) {
                        System.err.println("You need to be logged in to perform such action");
                    }

                }
            } else {
                System.out.println("Could not parse the command, please repeat it");
            }

        } while (true);

    }

}
