package julio.cardGame.cardGameServer.battle.cards.monsters;

public enum Races {
    Elf("Elf"),
    Goblin("Goblin"),
    Dragon("Dragon"),
    Kraken("Kraken"),
    Wizard("Wizard"),
    Ork("Ork"),
    Knight("Knight"),
    Troll("Troll");

    private final String race;

    Races(String race) {
        this.race = race;
    }

    public String getRace() {
        return this.race;
    }
}
