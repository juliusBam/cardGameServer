package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.common.models.CardRequestModel;

import java.io.IOException;
import java.util.UUID;

public class CardReader extends InputReader {

    CardReader() {
        super();
    }

    public CardRequestModel readCard() throws IOException {

        UUID cardId = new UUIDreader().readUUID();

        String cardName = new CardNameReader().readCardName();

        Double cardDmg = new DoubleReader().readDouble();

        return new CardRequestModel(cardId, cardName, cardDmg);

    }

}
