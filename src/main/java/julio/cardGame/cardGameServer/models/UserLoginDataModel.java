package julio.cardGame.cardGameServer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLoginDataModel {
    @JsonProperty("Username")
    public String userName;

    @JsonProperty("Password")
    public String password;

    public UserLoginDataModel(String name, String pwd) {
        this.userName = name;
        this.password = pwd;
    }

    public UserLoginDataModel(){}
}
