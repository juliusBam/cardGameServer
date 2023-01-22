package julio.cardGame.cardGameServer.battle.helpers.Fighter;

import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.battle.cards.Elements;
import julio.cardGame.cardGameServer.battle.cards.monsters.Monster;
import julio.cardGame.cardGameServer.battle.cards.monsters.Races;
import julio.cardGame.cardGameServer.battle.cards.spells.Spell;

import java.security.InvalidParameterException;

import static java.lang.Math.floor;

public class CardDmgCalculator {

    //overloading of the method calculateDmg
    public int calculateDmg(Monster attackingMonster, Monster defendingMonster) {

        if (attackingMonster == null || defendingMonster == null)
            throw new InvalidParameterException("Parameter is null in calculate Dmg");

        double dmgModificator = monsterRatio(attackingMonster.getRace(), attackingMonster.getType(), defendingMonster.getRace(), defendingMonster.getType());

        return (int) floor(attackingMonster.getDmg() * dmgModificator);
    }

    public int calculateDmg(Monster attackingMonster, Spell defendingSpell) {

        if (attackingMonster == null || defendingSpell == null)
            throw new InvalidParameterException("Parameter is null in calculate Dmg");

        double dmgModificator = elementRatio(attackingMonster.getType(), defendingSpell.getType());

        return (int) floor(attackingMonster.getDmg() * dmgModificator);
    }

    public int calculateDmg(Spell attackingSpell, Spell defendingSpell) {

        if (attackingSpell == null || defendingSpell == null)
            throw new InvalidParameterException("Parameter is null in calculate Dmg");

        double dmgModificator = elementRatio(attackingSpell.getType(), defendingSpell.getType());

        return (int) floor(attackingSpell.getDmg() * dmgModificator);
    }

    public int calculateDmg(Spell attackingSpell, Monster defendingMonster) {

        if (attackingSpell == null || defendingMonster == null)
            throw new InvalidParameterException("Parameter is null in calculate Dmg");

        double dmgModificator;

        if (defendingMonster.getRace() == Races.Kraken) {

            dmgModificator = 0;

        } else if (defendingMonster.getRace() == Races.Knight && attackingSpell.getType() == Elements.Water) {

            return Constants.WATERSPELL_VS_KNIGHT;

        } else {

            dmgModificator = elementRatio(attackingSpell.getType(), defendingMonster.getType());

        }

        return (int) floor(attackingSpell.getDmg() * dmgModificator);

    }

    private double elementRatio(Elements attackingEl, Elements defendingEl) {
        switch (attackingEl) {
            case Fire -> {
                if (defendingEl == Elements.Water) {
                    return 0.5;
                } else if (defendingEl == Elements.Normal) {
                    return 2;
                }
            }
            case Water -> {
                if (defendingEl == Elements.Fire) {
                    return 2;
                } else if (defendingEl == Elements.Normal) {
                    return 0.5;
                }
            }
            case Normal -> {
                if (defendingEl == Elements.Water) {
                    return 2;
                } else if (defendingEl == Elements.Fire) {
                    return 0.5;
                }
            }
        }
        return 1;
    }

    private double monsterRatio(Races attackingRace, Elements attackingEl, Races defendingRace, Elements defendingEl) {
        switch (attackingRace) {
            case Goblin -> {
                if (defendingRace == Races.Dragon) {
                    return 0;
                }
            }
            case Ork -> {
                if (defendingRace == Races.Wizard) {
                    return 0;
                }
            }
            case Dragon -> {
                if (defendingRace == Races.Elf && defendingEl == Elements.Fire) {
                    return 0;
                }
            }
        }
        return 1;
    }
}
