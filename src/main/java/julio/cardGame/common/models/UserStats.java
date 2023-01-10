package julio.cardGame.common.models;

public class UserStats {
    public int elo;

    public int wins;

    public int losses;

    public UserStats(int elo, int wins, int losses) {
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }
}
