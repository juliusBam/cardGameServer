package julio.cardGame.cardGameServer.battle.helpers.Fighter;

import julio.cardGame.cardGameServer.battle.cards.ICard;

//the class encapsulates the needed information after a round is played
//if the round is a draw the winner and loser are null.

public class CardFighterResult {
    private final ICard winner;
    private final ICard loser;
    private final int firstCardDmg;
    private final int secondCardDmg;
    private final boolean rickRoll;

    public CardFighterResult(ICard winner, ICard loser, int firstCardDmg, int secondCardDmg, boolean rickRoll) {
        this.winner = winner;
        this.loser = loser;
        this.firstCardDmg = firstCardDmg;
        this.secondCardDmg = secondCardDmg;
        this.rickRoll = rickRoll;
    }


    public ICard getWinner() {
        return winner;
    }

    public ICard getLoser() {
        return loser;
    }

    public int getFirstCardDmg() {
        return firstCardDmg;
    }

    public int getSecondCardDmg() {
        return secondCardDmg;
    }

    public boolean isRickRoll() {
        return rickRoll;
    }
}
