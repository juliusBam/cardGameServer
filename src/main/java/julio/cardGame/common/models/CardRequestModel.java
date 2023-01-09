package julio.cardGame.common.models;

import java.util.UUID;

public class CardRequestModel {
    public UUID Id;
    public String Name;
    public Double Damage;

    public CardRequestModel(UUID id, String name, Double damage) {
        Id = id;
        Name = name;
        Damage = damage;
    }

    public CardRequestModel() {

    }
}
