package elo;

import julio.cardGame.cardGameServer.battle.helpers.EloCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EloCalculationUnitTest {


    private final int higherElo = 1900;

    private final int lowerElo = 1700;

    @Test
    void calculateWinHigherElo() {

        //act
        int newElo = EloCalculator.calculateWinnerElo(higherElo, lowerElo);

        //assert
        assertEquals(1901, newElo);
    }

    @Test
    void calculateWinLowerElo() {

        int newElo = EloCalculator.calculateWinnerElo(lowerElo, higherElo);

        assertEquals(1706, newElo);
    }

    @Test
    void calculateLoseHigherElo() {

        int newElo = EloCalculator.calculateLoserElo(lowerElo, higherElo);

        assertEquals(1893, newElo);

    }

    @Test
    void calculateLoseLowerElo() {

        int newElo = EloCalculator.calculateLoserElo(higherElo, lowerElo);

        assertEquals(1698, newElo);

    }

    @Test
    void KFactor() {

        //arrange
        int lowerElo = 300;
        int mediumElo = 900;
        int higherElo = 1800;

        //act
        int k_low = EloCalculator.dynamicK_Factor(lowerElo);
        int k_medium = EloCalculator.dynamicK_Factor(mediumElo);
        int k_high = EloCalculator.dynamicK_Factor(higherElo);


        //assert
        assertEquals(k_low, EloCalculator.getLowEloK());
        assertEquals(k_medium, EloCalculator.getAvgEloK());
        assertEquals(k_high, EloCalculator.getHighEloK());
    }

}
