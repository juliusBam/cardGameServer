package julio.cardGame.cardGameClient.helpers.readers;

import java.io.IOException;

public class CardNameReader extends InputReader {
    CardNameReader() {
        super();
    }

    public String readCardName() throws IOException {

        System.out.print("Please enter the card name: ");

        String input = inputReader.readLine().trim();

        while(!clientInputValidator.validateCardName(input)) {
            input = inputReader.readLine().trim();
        }

        return input;
    }
}
