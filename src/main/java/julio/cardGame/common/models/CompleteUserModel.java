package julio.cardGame.common.models;


//composition is used, since we cannot inherit from 2 classes
public class CompleteUserModel {

    //public UserLoginDataModel userLoginData;

    public UserAdditionalDataModel userAdditionalData;

    public String userName;
    public int elo;

    public int wins;

    public int losses;

    public CompleteUserModel(String userName, UserAdditionalDataModel additionalData, int elo, int wins, int losses) {
        this.userName = userName;
        this.userAdditionalData = additionalData;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }

    public CompleteUserModel() {}
}
