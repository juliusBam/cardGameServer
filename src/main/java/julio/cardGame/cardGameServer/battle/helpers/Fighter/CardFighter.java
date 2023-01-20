package julio.cardGame.cardGameServer.battle.helpers.Fighter;

//Contains the logic for the damage calculation

import julio.cardGame.cardGameServer.battle.cards.ICard;

public class CardFighter {
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
