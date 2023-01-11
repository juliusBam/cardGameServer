package battleTests.tests.cardFighting;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.CardFactory;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.Elements;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.monsters.Races;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighter;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighterResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MonsterFightUnitTest {

    private final int goblinDmg = 10;
    private final int dragonDmg = 5;
    private final int wizardDmg = 5;
    private final int elfDmg = 5;
    private final int krakenDmg = 5;
    private final int orkDmg = 5;
    private final int knightDmg = 5;
    private final int trollDmg = 15;


    private ICard attackingMonster;
    private ICard defendingMonster;

    private CardFactory cardFactory;

    @BeforeAll
    void setup() {
        this.cardFactory = new CardFactory();
    }

    @Test
    void goblinVsDragon() {

        this.attackingMonster = this.cardFactory.createCard(Races.Goblin, "My Goblin", Elements.Normal, goblinDmg);
        this.defendingMonster = this.cardFactory.createCard(Races.Dragon, "My Dragon", Elements.Normal, dragonDmg);

        assertEquals(0, attackingMonster.fight(defendingMonster));
    }

    @Test
    void orkVsWizard() {

        this.attackingMonster = this.cardFactory.createCard(Races.Ork, "My Ork", Elements.Normal, orkDmg);
        this.defendingMonster = this.cardFactory.createCard(Races.Wizard, "My Wizard", Elements.Normal, wizardDmg);

        assertEquals(0, attackingMonster.fight(defendingMonster));
    }

    @Test
    void dragonVsElf() {

        this.attackingMonster = this.cardFactory.createCard(Races.Dragon, "My Dragon", Elements.Normal, dragonDmg);
        this.defendingMonster = this.cardFactory.createCard(Races.Elf, "My Elf", Elements.Fire, elfDmg);

        assertEquals(0, attackingMonster.fight(defendingMonster));
    }

    @Test
    void waterGoblinVsFireTroll() {

        this.attackingMonster = this.cardFactory.createCard(Races.Goblin, "My Goblin", Elements.Water, goblinDmg);
        this.defendingMonster = this.cardFactory.createCard(Races.Troll,"My Troll", Elements.Fire, trollDmg);

        CardFighter cardFighter = new CardFighter();

        assertEquals(goblinDmg, attackingMonster.fight(defendingMonster));
        assertEquals(trollDmg, defendingMonster.fight(attackingMonster));

        final CardFighterResult fightResult = cardFighter.returnFightResults(attackingMonster, defendingMonster);

        assertEquals(attackingMonster, fightResult.getLoser());

    }
}
