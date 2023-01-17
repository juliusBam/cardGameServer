package julio.cardGame.cardGameServer.battle.cards;

import julio.cardGame.cardGameServer.battle.cards.monsters.Monster;
import julio.cardGame.cardGameServer.battle.cards.monsters.Races;
import julio.cardGame.cardGameServer.battle.cards.spells.Spell;

import java.security.InvalidParameterException;

public class CardFactory {
    public ICard createCard(Races race, String newName, Elements newType, int newDmg) throws InvalidParameterException {
        if (newName == null) {

            throw new InvalidParameterException("New name is null");

        }
        if (newType == null) {

            throw new InvalidParameterException("New type is null");

        }
        if (race == null) {
            return new Spell(newName, newType, newDmg);
        } else {
            return new Monster(newName, newType, newDmg, race);
        }
    }
}

