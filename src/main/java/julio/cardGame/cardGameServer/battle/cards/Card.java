package julio.cardGame.cardGameServer.battle.cards;

import julio.cardGame.cardGameServer.battle.BattleUser;
import julio.cardGame.cardGameServer.battle.cards.monsters.Monster;
import julio.cardGame.cardGameServer.battle.cards.spells.Spell;
import julio.cardGame.cardGameServer.battle.helpers.Fighter.CardDmgCalculator;

import java.security.InvalidParameterException;

public abstract class Card implements ICard {
    private final String name;
    private final Elements type;

    private final int dmg;

    public Card(String newName, Elements newType, int newDmg) {

        this.name = newName;
        this.type = newType;
        this.dmg = newDmg;

    }

    public final void trade(BattleUser targetPlayer) {

    };

    public final void move(Deck targetDeck) {

    };

    public String getName() {
        return this.name;
    }

    public Elements getType() {
        return this.type;
    }

    public int getDmg() {
        return this.dmg;
    }

    public int fight(ICard cardToFight) {

        CardDmgCalculator cardDmgCalculator = new CardDmgCalculator();

        if (cardToFight == null) {
            throw new InvalidParameterException("Card to fight is null");
        }

        if (this instanceof Spell) {
            if (cardToFight instanceof Spell) {

                return cardDmgCalculator.calculateDmg((Spell) this, (Spell) cardToFight);

            } else if (cardToFight instanceof Monster) {

                return cardDmgCalculator.calculateDmg((Spell) this, (Monster) cardToFight);

            } else {

                throw new InvalidParameterException("Card to fight has no valid class");

            }
        } else if (this instanceof Monster){
            if (cardToFight instanceof Spell) {

                return cardDmgCalculator.calculateDmg((Monster) this, (Spell) cardToFight);

            } else if (cardToFight instanceof Monster) {

                return cardDmgCalculator.calculateDmg((Monster) this, (Monster) cardToFight);

            } else {

                throw new InvalidParameterException("Card to fight has no valid class");

            }
        } else {
            throw new InvalidParameterException("ICard is no valid class");
        }
    }
}

