package julio.cardGame.cardGameServer.application.battleLogic.gameParts;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighter;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighterResult;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages.IMessageFactory;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages.MessageFactory;
import julio.cardGame.common.models.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public int roundCounter = 0;

    //players are final
    private final User firstPlayer;
    private final User secondPlayer;

    public Game(UserInfo firstUser, UserInfo secondUser) {

        this.firstPlayer = new User(firstUser);

        //todo fetches the owned cards from the db
        //this.firstPlayer.createCollection();

        this.secondPlayer = new User(secondUser);
        //todo fetches the owned cards from the db
        //this.secondPlayer.createCollection();

    }

    public void createDecks() {

        this.firstPlayer.createDeck();
        this.secondPlayer.createDeck();

    }

    public List<String> playRound() {

        IMessageFactory messageFactory = new MessageFactory();

        //players create their decks
        this.createDecks();

        CardFighter cardFighter = new CardFighter();

        List<String> battleResult = new ArrayList<>();

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

                    battleResult.add(
                            messageFactory
                                    .createCardFightMsg(this.firstPlayer, firstPlayerFirstCard, this.secondPlayer, secondPlayerSecondCard, fightResult));

                    if (fightResult.getLoser() != null) {


                        System.out.println("Moving loser");
                    }

                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                break;
            }

        }

        battleResult.add(messageFactory.createEndGameMsg(this.calculateWinner()));

        return battleResult;

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

