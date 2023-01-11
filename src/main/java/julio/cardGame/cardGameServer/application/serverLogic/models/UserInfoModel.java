package julio.cardGame.cardGameServer.application.serverLogic.models;

import java.util.UUID;

public class UserInfoModel {
    public String userName;

    public UUID userID;

    public int elo;

    public UserInfoModel(String userName, UUID userID, int elo) {
        this.userName = userName;
        this.userID = userID;
        this.elo = elo;
    }
}
