package julio.cardGame.common.models;

import java.util.UUID;

public class UserInfo {
    public String userName;

    public UUID userID;

    public int elo;

    public UserInfo(String userName, UUID userID, int elo) {
        this.userName = userName;
        this.userID = userID;
        this.elo = elo;
    }
}
