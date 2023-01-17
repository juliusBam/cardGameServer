package julio.cardGame.cardGameServer.database.models;


//composition is used, since we cannot inherit from 2 classes
public class CompleteUserModel {

    //public UserLoginDataModel userLoginData;

    public UserAdditionalDataModel userAdditionalData;

    public String userName;

    public UserStatsModel userStatsModel;

    public int coins;

    public boolean isAdmin;

    public CompleteUserModel(String userName, UserAdditionalDataModel additionalData, UserStatsModel userStatsModel, int coins, boolean isAdmin) {
        this.userName = userName;
        this.userAdditionalData = additionalData;
        this.userStatsModel = userStatsModel;
        this.coins = coins;
    }

    public CompleteUserModel() {}
}
