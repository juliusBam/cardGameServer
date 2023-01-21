package julio.cardGame.cardGameServer.battle;

import julio.cardGame.cardGameServer.battle.cards.CardFactory;
import julio.cardGame.cardGameServer.battle.cards.Deck;
import julio.cardGame.cardGameServer.battle.cards.Elements;
import julio.cardGame.cardGameServer.battle.cards.ICard;
import julio.cardGame.cardGameServer.battle.cards.monsters.Races;
import julio.cardGame.cardGameServer.models.CardDeckModel;
import julio.cardGame.cardGameServer.models.UserInfoModel;
import julio.cardGame.cardGameServer.repositories.UserRepo;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

public class BattleUser {

    public UserInfoModel getInfo() {
        return info;
    }

    private UserInfoModel info;
    private final Deck deck;

    public BattleUser(UserInfoModel newUser) {

        this.info = newUser;
        this.deck = new Deck();

    }

    public void insertIntoDeck(ICard cardToInsert) {
        this.deck.addCard(cardToInsert);
    }

    public void createDeck() throws InvalidParameterException, SQLException, IllegalArgumentException {

        List<CardDeckModel> deckFromDb = new UserRepo().fetchDeckCards(this.info.userName);

        this.processCardResponse(deckFromDb);

    }

    public void processCardResponse(List<CardDeckModel> deckFromDb) throws IllegalArgumentException {

        CardFactory myFactory = new CardFactory();

        for (CardDeckModel card : deckFromDb) {

            Elements cardEl = Elements.valueOf(card.cardElement);
            Races cardRace = card.monsterRace == null ? null : Races.valueOf(card.monsterRace);

            this.insertIntoDeck(myFactory.createCard(cardRace, card.cardName, cardEl, (int) card.card_damage));

        }

    }

    public ICard getFirstCard() {
        return deck.getFirstCard();
    }

    public String getName() {
        return this.info.userName;
    }

    public boolean hasCards() {
        return this.deck.getDeckSize() > 0;
    }

    public int getDeckSize() {
        return this.deck.getDeckSize();
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void moveCardTo(BattleUser targetPlayer) {

        targetPlayer.deck.addCard(
                this.deck.removeFirstCard()
        );

    }

    public void putCardBack() {

        ICard playedCard = this.deck.removeFirstCard();

        this.deck.addCard(playedCard);

    }
}
