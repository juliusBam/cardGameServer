package julio.cardGame.common.models;

public class UserLoginDataModel {
    public String Username;

    public String Password;

    public UserLoginDataModel(String name, String pwd) {
        this.Username = name;
        this.Password = pwd;
    }

    public UserLoginDataModel(){}
}
