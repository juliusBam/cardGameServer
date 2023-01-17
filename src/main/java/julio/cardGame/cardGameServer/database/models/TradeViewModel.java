package julio.cardGame.cardGameServer.database.models;

public class TradeViewModel {

    public String userName;

    public int minimumDamage;

    public String requiredCardType;

    public String offeredCardName;

    public String offeredCardElement;

    public double offeredCardDamage;

    public TradeViewModel(String userName, int minimumDamage, String requiredCardType, String offeredCardName, String offeredCardElement, double offeredCardDamage) {
        this.userName = userName;
        this.minimumDamage = minimumDamage;
        this.requiredCardType = requiredCardType;
        this.offeredCardName = offeredCardName;
        this.offeredCardElement = offeredCardElement;
        this.offeredCardDamage = offeredCardDamage;
    }
}
