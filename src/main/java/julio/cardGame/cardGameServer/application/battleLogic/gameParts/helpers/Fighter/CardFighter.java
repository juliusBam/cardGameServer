package julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter;

//Contains the logic for the damage calculation

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;

public class CardFighter {

    //todo refactor to return the dmg, winner and loser in an object
    public CardFighterResult returnFightResults(ICard firstCard, ICard secondCard) {

        int dmgFirstCard = firstCard.fight(secondCard);
        int dmgSecondCard = secondCard.fight(firstCard);

        if (dmgFirstCard > dmgSecondCard) {
            return new CardFighterResult(firstCard, secondCard, dmgFirstCard, dmgSecondCard);
        }
        else if (dmgFirstCard < dmgSecondCard) {
            return new CardFighterResult(secondCard, firstCard, dmgFirstCard, dmgSecondCard);
        }
        //in the case of a draw
        return new CardFighterResult(null, null, dmgFirstCard, dmgSecondCard);
    }

}
