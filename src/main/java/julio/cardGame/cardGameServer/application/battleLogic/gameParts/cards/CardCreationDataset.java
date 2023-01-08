package julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.monsters.Races;

//simulates the output of the query
public class CardCreationDataset {
    public String name;
    public Elements type;
    public int dmg;
    public Races race;

    public CardCreationDataset (String newName, Elements newType, int newDmg, Races newRace){
        this.name = newName;
        this.type = newType;
        this.dmg = newDmg;
        this.race = newRace;
    }
}

