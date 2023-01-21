package battle;

import julio.cardGame.cardGameServer.battle.cards.CardFactory;
import julio.cardGame.cardGameServer.battle.cards.Elements;
import julio.cardGame.cardGameServer.battle.cards.ICard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpellFightUnitTest {

    private final int fireCardDmg = 5;
    private final int waterCardDmg = 5;
    private final int normalCardDmg = 5;

    private ICard attackingCard;
    private ICard defendingCard;
    
    private CardFactory cardFactory;

    @BeforeAll
    void setup() {
        this.cardFactory = new CardFactory();
    }

    @Test
    void testWaterVsFire() {
        final int expectedDmgWaterVsFire = waterCardDmg * 2;
        
        this.attackingCard = cardFactory.createCard(null,"My water", Elements.Water, waterCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My fire", Elements.Fire, fireCardDmg);

        assertEquals(expectedDmgWaterVsFire, attackingCard.fight(defendingCard));
    }

    @Test
    void testWaterVsNormal() {
        final int expectedDmgWaterVsNormal = (int) (waterCardDmg * 0.5);

        this.attackingCard = cardFactory.createCard(null,"My water", Elements.Water, waterCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My Normal", Elements.Normal, normalCardDmg);

        assertEquals(expectedDmgWaterVsNormal, attackingCard.fight(defendingCard));
    }

    @Test
    void testFireVsWater() {
        final int expectedDmgFireVsWater = (int) (fireCardDmg * 0.5);

        this.attackingCard = cardFactory.createCard(null,"My fire", Elements.Fire, fireCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My water", Elements.Water, waterCardDmg);

        assertEquals(expectedDmgFireVsWater, attackingCard.fight(defendingCard));
    }

    @Test
    void testFireVsNormal() {
        final int expectedDmgFireVsNormal = fireCardDmg * 2;

        this.attackingCard = cardFactory.createCard(null,"My fire", Elements.Fire, fireCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My Normal", Elements.Normal, normalCardDmg);

        assertEquals(expectedDmgFireVsNormal, attackingCard.fight(defendingCard));
    }

    @Test
    void testNormalVsFire() {
        final int expectedDmgNormalVsFire = (int) (normalCardDmg * 0.5);

        this.attackingCard = cardFactory.createCard(null,"My Normal", Elements.Normal, normalCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My fire", Elements.Fire, fireCardDmg);

        assertEquals(expectedDmgNormalVsFire, attackingCard.fight(defendingCard));
    }

    @Test
    void testNormalVsWater() {
        final int expectedDmgNormalVsWater = normalCardDmg * 2;

        this.attackingCard = cardFactory.createCard(null,"My Normal", Elements.Normal, normalCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My water", Elements.Water, waterCardDmg);

        assertEquals(expectedDmgNormalVsWater, attackingCard.fight(defendingCard));
    }

    @Test
    void testNormalVsNormal() {
        this.attackingCard = cardFactory.createCard(null,"My Normal", Elements.Normal, normalCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My Normal 2", Elements.Normal, normalCardDmg);

        assertEquals(normalCardDmg, attackingCard.fight(defendingCard));
    }

    @Test
    void testFireVsFire() {
        this.attackingCard = cardFactory.createCard(null,"My fire", Elements.Fire, fireCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My fire 2", Elements.Fire, fireCardDmg);

        assertEquals(fireCardDmg, attackingCard.fight(defendingCard));
    }

    @Test
    void testWaterVsWater() {
        this.attackingCard = cardFactory.createCard(null,"My water", Elements.Water, waterCardDmg);
        this.defendingCard = cardFactory.createCard(null,"My water 2", Elements.Water, waterCardDmg);

        assertEquals(waterCardDmg, attackingCard.fight(defendingCard));
    }
}
