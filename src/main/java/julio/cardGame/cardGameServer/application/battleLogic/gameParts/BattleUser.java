package julio.cardGame.cardGameServer.application.battleLogic.gameParts;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.*;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.monsters.Races;
import julio.cardGame.cardGameServer.application.dbLogic.models.CardDeckModel;
import julio.cardGame.cardGameServer.application.dbLogic.models.UserInfoModel;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;

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

        List<CardDeckModel> deckFromDb = new UserRepo().fetchDeckCards(this.info.userID);

        this.processCardResponse(deckFromDb);



    }

    //to do use function pointer
    public void processCardResponse(List<CardDeckModel> deckFromDb) throws IllegalArgumentException {

        CardFactory myFactory = new CardFactory();

        //try {

            for (CardDeckModel card: deckFromDb) {

                Elements cardEl = Elements.valueOf(card.cardElement);
                Races cardRace =  card.monsterRace == null ? null : Races.valueOf(card.monsterRace);

                //we execute the reflection of the passed method
                //method.invoke(this, myFactory.createCard(el.race, el.name, el.type, el.dmg));
                //if (actionType == 'd') {

                this.insertIntoDeck(myFactory.createCard(cardRace, card.cardName, cardEl, (int)card.card_damage));

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

    public Deck getDeck(){return this.deck;}

    public void moveCardTo(BattleUser targetPlayer) {

        targetPlayer.deck.addCard(
                this.deck.removeFirstCard()
        );

    }

}