package julio.cardGame.cardGameServer.database.models;

public class ScoreModel extends StatsModel {
    public String userName;

    ScoreModel(){super();}

    public ScoreModel(int wins, int losses, double winRate, int elo, String userName) {
        super(wins, losses, winRate, elo);
        this.userName = userName;
    }

}
