package julio.cardGame.common;

public enum CardTypes {
    SPELL("Spell"),

    MONSTER("Monster");

    private final String cardType;

    CardTypes(String type) {
        cardType = type;
    }

    public String getCardType() {
        return this.cardType;
    }
}
