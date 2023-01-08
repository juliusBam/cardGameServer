package julio.cardGame.cardGameClient.helpers.readers;

import java.io.IOException;

public class DoubleReader extends InputReader{
    DoubleReader() {
        super();
    }

    public Double readDouble() throws IOException {

        System.out.print("Please enter the card damage: ");

        String input = inputReader.readLine().trim();

        Double cardDmg = clientInputValidator.validateDouble(input);

        while (cardDmg == null) {
            System.out.print("Invalid string, please enter a decimal number: ");
            input = inputReader.readLine().trim();

            cardDmg = clientInputValidator.validateDouble(input);
        }

        return cardDmg;

    }
}
