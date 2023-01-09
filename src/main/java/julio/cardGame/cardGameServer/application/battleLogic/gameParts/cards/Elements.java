package julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards;

public enum Elements {
    Fire("Fire"),
    Water("Water"),
    Normal("Normal");

    private final String element;

    Elements(String element) {
        this.element = element;
    }

    public String getElement() {
        return this.element;
    }
}

