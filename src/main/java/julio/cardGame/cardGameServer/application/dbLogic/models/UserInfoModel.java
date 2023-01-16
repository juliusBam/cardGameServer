package julio.cardGame.cardGameServer.application.dbLogic.models;

import java.util.UUID;

public class UserInfoModel {
    public String userName;

    public int elo;

    public UserInfoModel(String userName, int elo) {
        this.userName = userName;
        this.elo = elo;
    }
}
