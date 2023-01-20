package dataTransformation.test;

import julio.cardGame.cardGameServer.battle.helpers.EloCalculator;
import julio.cardGame.cardGameServer.database.db.DataTransformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataTransformationUnitTest {


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

    }

    @Test
    void calculateLoseHigherElo() {

    }

    @Test
    void calculateLoseLowerElo() {

    }

}
