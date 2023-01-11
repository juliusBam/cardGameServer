package julio.cardGame.cardGameServer.application.serverLogic.models;

public class UserStatsModel {
    public int elo;

    public int wins;

    public int losses;

    public UserStatsModel(int elo, int wins, int losses) {
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }
}
