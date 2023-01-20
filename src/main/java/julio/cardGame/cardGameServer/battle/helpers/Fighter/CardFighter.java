package julio.cardGame.cardGameServer.battle.helpers.Fighter;

//Contains the logic for the damage calculation

import julio.cardGame.cardGameServer.battle.BattleUser;
import julio.cardGame.cardGameServer.battle.cards.ICard;

import java.util.Random;

public class CardFighter {

    public CardFighterResult returnFightResults(ICard firstCard, BattleUser firstPlayer, ICard secondCard, BattleUser secondPlayer) {

        boolean rickRolled = false;

        int rickRollFirstCard = this.calculateRickRoll(firstPlayer, secondPlayer);
        int rickRollSecondCard = this.calculateRickRoll(secondPlayer, firstPlayer);

        int dmgFirstCard = rickRollFirstCard * firstCard.fight(secondCard);
        int dmgSecondCard = rickRollSecondCard * secondCard.fight(firstCard);

        if (rickRollFirstCard != 1 || rickRollSecondCard != 1)
            rickRolled = true;

        if (dmgFirstCard > dmgSecondCard) {
            return new CardFighterResult(
                    firstCard,
                    secondCard,
                    dmgFirstCard,
                    dmgSecondCard,
                    rickRolled
            );
        }
        else if (dmgFirstCard < dmgSecondCard) {
            return new CardFighterResult(
                    secondCard,
                    firstCard,
                    dmgFirstCard,
                    dmgSecondCard,
                    rickRolled
            );
        }
        //in the case of a draw
        return new CardFighterResult(null, null, dmgFirstCard, dmgSecondCard, isRickRoll(dmgFirstCard, firstCard, dmgSecondCard, secondCard));
    }

    private int calculateRickRoll(BattleUser firstPlayer, BattleUser secondPlayer) {

        Random rand = new Random();

        int eloDiff = firstPlayer.getInfo().elo - secondPlayer.getInfo().elo;

        //if the player has a lower elo than the opponent the chance of critical strike grows
        if (eloDiff < -30) {

            int percentage = rand.nextInt(100);

            if (percentage < 18) {
                return 3;
            }


            return 1;

        }

        return 1;

    }

    private boolean isRickRoll(int dmgFirstCard, ICard firstCard, int dmgSecondCard, ICard secondCard) {

        return (dmgFirstCard > 2 * firstCard.getDmg() || dmgSecondCard > 2 * firstCard.getDmg());

    }

}
