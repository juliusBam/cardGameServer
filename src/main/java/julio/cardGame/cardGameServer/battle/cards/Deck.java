package julio.cardGame.cardGameServer.battle.cards;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Deck {
    //todo refactor as List --> is interface
    private final List<ICard> cards;

    public Deck() {
        this.cards = new ArrayList<ICard>();
    }

    public Deck(ArrayList<ICard> newCards) {
        this.cards = newCards;
    }

    public void addCard(ICard newCard) {
        if (newCard == null) {
            throw new InvalidParameterException("New card is null");
        }

        this.cards.add(newCard);
    }

    public ICard getFirstCard() {
        return cards.get(0);
    }

    public ICard removeFirstCard() {
        return cards.remove(0);
    }

    public void removeCard(int position) {
        this.cards.remove(position);
    }

    public int getDeckSize() {
        return this.cards.size();
    }
}
