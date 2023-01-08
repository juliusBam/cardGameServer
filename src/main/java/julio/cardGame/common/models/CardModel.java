package julio.cardGame.common.models;

import java.util.UUID;

public class CardModel {
    public UUID Id;
    public String Name;
    public Double Damage;

    public CardModel(UUID id, String name, Double damage) {
        Id = id;
        Name = name;
        Damage = damage;
    }

    public CardModel() {

    }
}
