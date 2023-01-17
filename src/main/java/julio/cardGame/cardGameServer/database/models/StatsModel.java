package julio.cardGame.cardGameServer.database.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatsModel {
    @JsonProperty("Wins")
    public int wins;

    @JsonProperty("Losses")
    public int losses;

    @JsonProperty("WinRate")
    public double winRate;

    @JsonProperty("Elo")
    public int elo;

    public StatsModel() {}

    public StatsModel(int wins, int losses, double winRate, int elo) {
        this.wins = wins;
        this.losses = losses;
        this.winRate = winRate;
        this.elo = elo;
    }
}
