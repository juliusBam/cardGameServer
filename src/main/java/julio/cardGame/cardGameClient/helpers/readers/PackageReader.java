package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.cardGameServer.application.dbLogic.models.CardRequestModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackageReader extends InputReader {

    public PackageReader() {
        super();
    }

    public List<CardRequestModel> readPackage() throws IOException {

        System.out.println("Package creation, please follow the instructions");

        List<CardRequestModel> pack = new ArrayList<>();

        while (true) {

            int i;
            for (i = 1; i <= 5; ++i) {
                System.out.printf("=================== CARD %s ===================\n", i);
                pack.add(new CardReader()
                        .readCard());
            }

            if (pack.size() == 5) {
                System.out.println();
                break;
            }

            pack = new ArrayList<>();
            System.out.println("Invalid package size, it should contain 5 cards, try again");

        }

        return pack;

    }

}
