package julio.cardGame.common.models;

public class StatsModel {
    public int Wins;

    public int Losses;

    public double WinRate;

    public int Elo;

    public StatsModel() {}

    public StatsModel(int wins, int losses, double winRate, int elo) {
        Wins = wins;
        Losses = losses;
        WinRate = winRate;
        Elo = elo;
    }
}
