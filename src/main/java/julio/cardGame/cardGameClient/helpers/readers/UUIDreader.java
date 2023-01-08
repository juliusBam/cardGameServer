package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.cardGameClient.helpers.ClientInputValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class UUIDreader extends InputReader {

    public UUIDreader() {super();
    }

    public UUID readUUID() throws IOException {

        //final ClientInputValidator clientInputValidator = new ClientInputValidator();

        //final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Please enter the desired id (UUID): ");

        String input = inputReader.readLine().trim();

        UUID uuid = clientInputValidator.validateUUID(input);

        while (uuid == null) {
            System.out.print("Invalid UUID, please try again: ");
            input = inputReader.readLine().trim();

            uuid = clientInputValidator.validateUUID(input);
        }

        return uuid;

    }

}
