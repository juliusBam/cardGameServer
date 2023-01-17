package julio.cardGame.cardGameServer.battle.cards.monsters;

import julio.cardGame.cardGameServer.battle.cards.Card;
import julio.cardGame.cardGameServer.battle.cards.Elements;

//Kept it for future proofing, since maybe there will be a common method between monsters
public final class Monster extends Card {

    private final Races race;
    public Monster(String newName, Elements newType, int newDmg, Races newRace) {

        super(newName, newType, newDmg);
        this.race = newRace;

    }

    public Races getRace() {
        return this.race;
    }

}
