package julio.cardGame.cardGameClient.helpers.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeckReader extends InputReader {

    public DeckReader() {super();}

    public List<String> readDeck() throws IOException {

        System.out.println("Deck creation, please follow the instructions");

        List<String> deck = new ArrayList<>();

        while (true) {

            int i;
            for (i = 1; i <= 4; ++i) {
                System.out.printf("=================== CARD ID %s ===================\n", i);
                deck.add(new UUIDreader()
                        .readUUID().toString());
            }

            if (deck.size() == 4) {
                System.out.println();
                break;
            }

            deck = new ArrayList<>();
            System.out.println("Invalid deck size, it should contain 4 cards");

        }

        return deck;

    }
}
