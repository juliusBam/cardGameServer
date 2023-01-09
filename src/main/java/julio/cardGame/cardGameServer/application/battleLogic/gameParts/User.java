package julio.cardGame.cardGameServer.application.battleLogic.gameParts;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.CardCreationDataset;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.CardFactory;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.Deck;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.services.CardsFetcher;
import julio.cardGame.common.models.UserInfo;

import java.security.InvalidParameterException;
import java.util.List;

public class User {

    private UserInfo info;
    private final Deck deck;
    private final Deck collection;

    public User(UserInfo newUser) {

        this.info = newUser;
        this.deck = new Deck();
        this.collection = new Deck();

    }

    public void insertIntoDeck(ICard cardToInsert) {
        this.deck.addCard(cardToInsert);
    }

    public void insertIntoCollection(ICard cardToInsert) {
        this.collection.addCard(cardToInsert);
    }

    public void createDeck() throws InvalidParameterException {

        CardsFetcher cardsFetcher = new CardsFetcher();

        try {

            List<CardCreationDataset> creationResponse = cardsFetcher.getDeck(this.info.userID);

            this.processCardResponse(creationResponse);

        } catch (InvalidParameterException e) {

            throw e;

        }

    }

    //to do use function pointer
    public void processCardResponse(List<CardCreationDataset> creationResponse) {

        CardFactory myFactory = new CardFactory();

        //try {

            for (CardCreationDataset el: creationResponse) {

                //we execute the reflection of the passed method
                //method.invoke(this, myFactory.createCard(el.race, el.name, el.type, el.dmg));
                //if (actionType == 'd') {

                    this.insertIntoDeck(myFactory.createCard(el.race, el.name, el.type, el.dmg));

                /*} else if (actionType == 'c'){

                    this.insertIntoCollection(myFactory.createCard(el.race, el.name, el.type, el.dmg));
                } else {

                    throw new InvalidParameterException("Wrong container type");

                }*/
            }

        /*} catch (InvalidParameterException e) {

            System.err.println(e.getMessage());

        }*/

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

}
