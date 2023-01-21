package julio.cardGame.cardGameServer.battle.helpers;

public class EloCalculator {

    //the k factor determines how fast players will climb the ladder
    private final static int LOW_ELO_K = 32;

    private final static int AVG_ELO_K = 16;

    private final static int HIGH_ELO_K = 8;

    private final static int WIN_VALUE = 1;

    private final static int LOSE_VALUE = 0;

    private final static float PROBABILITY_CONSTANT = 400.0f;

    private static float winningProbabilityFirstPlayer(int eloFirstPlayer, int eloSecondPlayer) {

        return 1.0f / (1.0f + ((float) (Math.pow(10, (eloSecondPlayer - eloFirstPlayer) / PROBABILITY_CONSTANT))));

    }

    public static int calculateWinnerElo(int winnerElo, int loserElo) {

        float prob = winningProbabilityFirstPlayer(winnerElo, loserElo);

        float newElo = winnerElo + dynamicK_Factor(winnerElo) * (WIN_VALUE - prob);

        return Math.max((int) newElo, 0);

    }

    public static int calculateLoserElo(int winnerElo, int loserElo) {

        float prob = winningProbabilityFirstPlayer(loserElo, winnerElo);

        float newElo = loserElo + dynamicK_Factor(loserElo) * (LOSE_VALUE - prob);

        return Math.max((int) newElo, 0);

    }

    public static int dynamicK_Factor(int elo) {

        if (elo < 800)
            return LOW_ELO_K;

        if (elo < 1600)
            return AVG_ELO_K;

        return HIGH_ELO_K;

    }

    public static int getLowEloK() {return LOW_ELO_K;}

    public static int getAvgEloK() {return AVG_ELO_K;}

    public static int getHighEloK() {return HIGH_ELO_K;}

}
