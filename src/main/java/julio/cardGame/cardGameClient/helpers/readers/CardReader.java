package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.cardGameClient.helpers.ClientInputValidator;
import julio.cardGame.common.models.CardModel;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CardReader extends InputReader {

    CardReader() {
        super();
    }

    public CardModel readCard() throws IOException {

        UUID cardId = new UUIDreader().readUUID();

        String cardName = new CardNameReader().readCardName();

        Double cardDmg = new DoubleReader().readDouble();

        return new CardModel(cardId, cardName, cardDmg);

    }

}
