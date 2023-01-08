package julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;

public class CardFighterResult {
    private final ICard winner;
    private final ICard loser;
    private final int firstCardDmg;
    private final int secondCardDmg;

    public CardFighterResult(ICard winner, ICard loser, int firstCardDmg, int secondCardDmg) {
        this.winner = winner;
        this.loser = loser;
        this.firstCardDmg = firstCardDmg;
        this.secondCardDmg = secondCardDmg;
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
}
