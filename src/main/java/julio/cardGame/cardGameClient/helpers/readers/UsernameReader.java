package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.cardGameClient.helpers.ClientInputValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UsernameReader extends InputReader {
    public UsernameReader() {super();}

    public String readUsername() throws IOException {

        System.out.print("Please enter the desired username: ");

        String input = this.inputReader.readLine().trim();

        while (!this.clientInputValidator.validateUsername(input)) {
            input = this.inputReader.readLine().trim();
        }

        return input;

    }
}
