package julio.cardGame.cardGameServer.models;


//composition is used, since we cannot extend 2 classes
public class CompleteUserModel {

    public String userName;

    public int coins;

    public boolean isAdmin;

    public boolean active;
    public UserAdditionalDataModel userAdditionalData;
    public UserStatsModel userStatsModel;

    public CompleteUserModel(String userName, UserAdditionalDataModel additionalData, UserStatsModel userStatsModel, int coins, boolean isAdmin, boolean active) {
        this.userName = userName;
        this.userAdditionalData = additionalData;
        this.userStatsModel = userStatsModel;
        this.coins = coins;
        this.isAdmin = isAdmin;
        this.active = active;
    }

    public CompleteUserModel() {}
}
