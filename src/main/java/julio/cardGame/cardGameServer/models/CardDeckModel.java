package julio.cardGame.cardGameServer.models;

public class CardDeckModel {
    public String cardName;

    public double card_damage;

    public String cardElement;

    public String cardType;

    public String monsterRace;

    public CardDeckModel(String cardName, double card_damage, String cardElement, String cardType, String monsterRace) {
        this.cardName = cardName;
        this.card_damage = card_damage;
        this.cardElement = cardElement;
        this.cardType = cardType;
        this.monsterRace = monsterRace;
    }

}
