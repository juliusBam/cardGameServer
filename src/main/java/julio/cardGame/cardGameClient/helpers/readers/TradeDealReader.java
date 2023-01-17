package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.cardGameServer.database.models.TradeModel;

import java.io.IOException;
import java.util.UUID;

public class TradeDealReader extends InputReader {
    public TradeDealReader() {super();
    }

    public TradeModel readTradeDeal() throws IOException {

        System.out.println("========== Trade id ==========");
        UUID tradeId = new UUIDreader().readUUID();

        System.out.println("========== Card to trade ==========");
        UUID cardId = new UUIDreader().readUUID();

        String cardType = new TypeReader().readType();

        System.out.println("========== Minimum damage ==========");
        int minimumDmg = new DoubleReader().readDouble().intValue();

        return new TradeModel(tradeId, cardId, cardType, minimumDmg);
    }
}
