package battle;

import julio.cardGame.cardGameServer.battle.BattleUser;
import julio.cardGame.cardGameServer.battle.cards.CardFactory;
import julio.cardGame.cardGameServer.battle.cards.Elements;
import julio.cardGame.cardGameServer.battle.cards.ICard;
import julio.cardGame.cardGameServer.battle.cards.monsters.Races;
import julio.cardGame.cardGameServer.battle.helpers.Fighter.CardFighter;
import julio.cardGame.cardGameServer.battle.helpers.Fighter.CardFighterResult;
import julio.cardGame.cardGameServer.models.UserInfoModel;
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
    private final int orkDmg = 5;
    private final int trollDmg = 15;

    private BattleUser firstPlayer;

    private BattleUser secondPlayer;


    private ICard firstCard;
    private ICard secondCard;

    private CardFactory cardFactory;

    private CardFighter cardFighter;

    @BeforeAll
    void setup() {

        this.cardFactory = new CardFactory();

        this.cardFighter = new CardFighter();

        this.firstPlayer = new BattleUser(
                new UserInfoModel(
                        "first player",
                        500
                )
        );

        this.secondPlayer = new BattleUser(
                new UserInfoModel(
                        "second player",
                        500
                )
        );

    }

    @Test
    void goblinVsDragon() {

        this.firstCard = this.cardFactory.createCard(Races.Goblin, "My Goblin", Elements.Normal, goblinDmg);
        this.secondCard = this.cardFactory.createCard(Races.Dragon, "My Dragon", Elements.Normal, dragonDmg);


        assertEquals(0, firstCard.fight(secondCard));
        assertEquals(secondCard, cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer).getWinner());

    }

    @Test
    void orkVsWizard() {

        this.firstCard = this.cardFactory.createCard(Races.Ork, "My Ork", Elements.Normal, orkDmg);
        this.secondCard = this.cardFactory.createCard(Races.Wizard, "My Wizard", Elements.Normal, wizardDmg);

        assertEquals(0, firstCard.fight(secondCard));
        assertEquals(secondCard, cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer).getWinner());
    }

    @Test
    void dragonVsElf() {

        this.firstCard = this.cardFactory.createCard(Races.Dragon, "My Dragon", Elements.Normal, dragonDmg);
        this.secondCard = this.cardFactory.createCard(Races.Elf, "My Elf", Elements.Fire, elfDmg);

        assertEquals(0, firstCard.fight(secondCard));
        assertEquals(secondCard, cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer).getWinner());

    }

    @Test
    void waterGoblinVsFireTroll() {

        this.firstCard = this.cardFactory.createCard(Races.Goblin, "My Goblin", Elements.Water, goblinDmg);
        this.secondCard = this.cardFactory.createCard(Races.Troll,"My Troll", Elements.Fire, trollDmg);

        assertEquals(goblinDmg, firstCard.fight(secondCard));
        assertEquals(trollDmg, secondCard.fight(firstCard));

        final CardFighterResult fightResult = cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer);

        assertEquals(firstCard, fightResult.getLoser());
        assertEquals(secondCard, fightResult.getWinner());

    }
}
