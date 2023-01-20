package julio.cardGame.cardGameServer.models;

public class UserInfoModel {
    public String userName;

    public int elo;

    public UserInfoModel(String userName, int elo) {
        this.userName = userName;
        this.elo = elo;
    }
}
