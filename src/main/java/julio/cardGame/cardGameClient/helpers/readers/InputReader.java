package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.cardGameClient.helpers.ClientInputValidator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class InputReader {

    protected final ClientInputValidator clientInputValidator;

    protected final BufferedReader inputReader;

    InputReader() {
        this.clientInputValidator = new ClientInputValidator();
        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
    }

}
