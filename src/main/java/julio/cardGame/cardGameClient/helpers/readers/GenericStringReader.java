package julio.cardGame.cardGameClient.helpers.readers;

import java.io.IOException;

public class GenericStringReader extends InputReader{
    public GenericStringReader() {super();
    }

    public String readString() throws IOException {

        System.out.print("Enter the desired string: ");

        String input;

        while (true) {
            input = this.inputReader.readLine().trim();

            if (!input.isBlank() && !input.isEmpty()) {
                break;
            }

            System.out.print("Invalid string, try again: ");

        }

        return input;

    }
}
