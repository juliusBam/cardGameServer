package julio.cardGame.cardGameServer.models;

import java.util.UUID;

public class PackageModel {

    public UUID packageID;

    public UUID firstCardID;

    public UUID secondCardID;

    public UUID thirdCardID;

    public UUID fourthCardID;

    public UUID fifthCardID;

    public PackageModel(UUID packageID, UUID firstCardID, UUID secondCardID, UUID thirdCardID, UUID fourthCardID, UUID fifthCardID) {
        this.packageID = packageID;
        this.firstCardID = firstCardID;
        this.secondCardID = secondCardID;
        this.thirdCardID = thirdCardID;
        this.fourthCardID = fourthCardID;
        this.fifthCardID = fifthCardID;
    }
}
