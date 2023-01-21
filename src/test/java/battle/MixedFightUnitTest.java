package battle;

import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.battle.BattleUser;
import julio.cardGame.cardGameServer.battle.cards.CardFactory;
import julio.cardGame.cardGameServer.battle.cards.Elements;
import julio.cardGame.cardGameServer.battle.cards.ICard;
import julio.cardGame.cardGameServer.battle.cards.monsters.Races;
import julio.cardGame.cardGameServer.battle.helpers.Fighter.CardFighter;
import julio.cardGame.cardGameServer.models.UserInfoModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static java.lang.Math.floor;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MixedFightUnitTest {

    private final int waterSpellDmg = 20;

    private final int fireSpellDmg = 15;

    private final int normalSpellDmg = 10;

    private final int knightDmg = 10;

    private final int krakenDmg = 10;

    private final int waterGoblinDmg = 5;

    private final int fireGoblinDmg = 5;

    private final int normalOrkDmg = 5;

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
    void waterSpellVsKnight() {

        this.firstCard = this.cardFactory.createCard(null, "waterSpell", Elements.Water, waterSpellDmg);
        this.secondCard = this.cardFactory.createCard(Races.Knight, "knight", Elements.Normal, knightDmg);

        assertEquals(Constants.WATERSPELL_VS_KNIGHT, firstCard.fight(secondCard));
        assertEquals(firstCard, cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer).getWinner());

    }

    @Test
    void spellVsKraken() {

        this.firstCard = this.cardFactory.createCard(Races.Kraken, "kraken", Elements.Normal, krakenDmg);

        this.secondCard = this.cardFactory.createCard(null, "waterSpell", Elements.Water, waterSpellDmg);

        assertEquals(0, secondCard.fight(firstCard));
        assertEquals(firstCard, cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer).getWinner());

    }

    @Test
    void waterSpellVsFireMonster() {

        this.firstCard = this.cardFactory.createCard(null, "waterSpell", Elements.Water, waterSpellDmg);

        this.secondCard = this.cardFactory.createCard(Races.Goblin, "fire goblin", Elements.Fire, fireGoblinDmg);

        assertEquals((int) floor(fireGoblinDmg * 0.5), secondCard.fight(firstCard));
        assertEquals(waterSpellDmg * 2, firstCard.fight(secondCard));

        assertEquals(firstCard, cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer).getWinner());


    }

    @Test
    void normalSpellVsWaterMonster() {

        this.firstCard = this.cardFactory.createCard(null, "normalSpell", Elements.Normal, normalSpellDmg);

        this.secondCard = this.cardFactory.createCard(Races.Goblin, "water goblin", Elements.Water, waterGoblinDmg);

        assertEquals((int) floor(waterGoblinDmg * 0.5), secondCard.fight(firstCard));
        assertEquals(normalSpellDmg * 2, firstCard.fight(secondCard));

        assertEquals(firstCard, cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer).getWinner());

    }

    @Test
    void fireSpellVsNormalMonster() {

        this.firstCard = this.cardFactory.createCard(null, "fire spell", Elements.Fire, fireSpellDmg);

        this.secondCard = this.cardFactory.createCard(Races.Ork, "normal ork", Elements.Normal, normalOrkDmg);

        assertEquals((int) floor(normalOrkDmg * 0.5), secondCard.fight(firstCard));
        assertEquals(fireSpellDmg * 2, firstCard.fight(secondCard));

        assertEquals(firstCard, cardFighter.returnFightResults(firstCard, firstPlayer, secondCard, secondPlayer).getWinner());

    }



}
