package dataTransformation.test;

import julio.cardGame.cardGameServer.database.db.DataTransformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataTransformationUnitTest {


    private final int higherElo = 1000;

    private final int lowerElo = 800;

    @Test
    void calculateWinHigherElo() {

        //act
        int newElo = DataTransformation.calculateWinnerElo(higherElo, lowerElo);

        //assert
        assertEquals(1016, newElo);
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
