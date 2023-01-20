package julio.cardGame.cardGameServer.database.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class ChangeUserStatusModel {

    @JsonProperty("Username")
    public String userName;

    public ChangeUserStatusModel(){}

    public ChangeUserStatusModel(String newUserName) {
        this.userName = newUserName;
    }
}
