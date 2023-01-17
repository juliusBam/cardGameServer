package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.cardGameServer.battle.cards.CardTypes;

import java.io.IOException;

public class TypeReader extends InputReader {
    public TypeReader() {
        super();
    }

    public String readType() throws IOException {

        System.out.print("Please enter the desired card type: ");

        String input;

        while (true) {
            input = inputReader.readLine().trim();

            if (input.equals(CardTypes.MONSTER.getCardType())
                    || input.equals(CardTypes.SPELL.getCardType()))
                break;

            System.out.print("Invalid card type (monster/spell), try again: ");
        }

        return input;

    }
}
