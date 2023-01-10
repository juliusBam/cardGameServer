package julio.cardGame.common.models;


//composition is used, since we cannot inherit from 2 classes
public class CompleteUserModel {

    //public UserLoginDataModel userLoginData;

    public UserAdditionalDataModel userAdditionalData;

    public String userName;

    public UserStats userStats;

    public int coins;

    public CompleteUserModel(String userName, UserAdditionalDataModel additionalData, UserStats userStats, int coins) {
        this.userName = userName;
        this.userAdditionalData = additionalData;
        this.userStats = userStats;
        this.coins = coins;
    }

    public CompleteUserModel() {}
}
