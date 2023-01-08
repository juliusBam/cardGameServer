package julio.cardGame.cardGameServer.application.battleLogic.gameParts;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighter;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighterResult;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages.IMessageFactory;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages.MessageFactory;

public class Game {
    public int roundCounter = 0;

    //players are final
    private final User firstPlayer;
    private final User secondPlayer;

    public Game(String firstName, String secondName) {

        this.firstPlayer = new User(firstName);

        //todo fetches the owned cards from the db
        this.firstPlayer.createCollection();

        this.secondPlayer = new User(secondName);
        //todo fetches the owned cards from the db
        this.secondPlayer.createCollection();

    }

    public void createDecks() {

        this.firstPlayer.createDeck();
        this.secondPlayer.createDeck();

    }

    public void playRound() {

        IMessageFactory messageFactory = new MessageFactory();

        //players create their decks
        this.createDecks();

        CardFighter cardFighter = new CardFighter();

        while (this.playersHaveCards()) {

            if (this.roundCounter >= 100) {
                System.out.println(messageFactory.createRoundLimitMsg());
                break;
            }
            this.roundCounter++;
            try {

                final ICard firstPlayerFirstCard = firstPlayer.getFirstCard();
                final ICard secondPlayerSecondCard = secondPlayer.getFirstCard();

                CardFighterResult fightResult = cardFighter.returnFightResults(firstPlayerFirstCard, secondPlayerSecondCard);

                if (fightResult == null) {
                    throw new RuntimeException("Fight result is null");
                } else {
                    System.out.println(messageFactory.createCardFightMsg(this.firstPlayer, firstPlayerFirstCard, this.secondPlayer, secondPlayerSecondCard, fightResult));
                    if (fightResult.getLoser() != null) {
                        System.out.println("Moving loser");
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                break;
            }

        }

        System.out.println(messageFactory.createEndGameMsg(this.calculateWinner()));

    }

    public User getFirstPlayer() {
        return firstPlayer;
    }

    public User getSecondPlayer() {
        return secondPlayer;
    }

    private boolean playersHaveCards() {
        return this.firstPlayer.hasCards() && this.secondPlayer.hasCards();
    }

    private User calculateWinner() {
        int firstPlayerCards = this.firstPlayer.getDeckSize();
        int secondPlayerCards = this.secondPlayer.getDeckSize();

        if (firstPlayerCards == secondPlayerCards) {
            return null;
        } else {
            return firstPlayerCards > secondPlayerCards ? this.firstPlayer : this.secondPlayer;
        }
    }

}

