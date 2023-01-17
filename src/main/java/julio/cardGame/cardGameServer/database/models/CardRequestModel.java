package julio.cardGame.cardGameServer.database.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CardRequestModel {
    @JsonProperty("Id")
    public UUID id;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Damage")
    public Double damage;

    public CardRequestModel(UUID id, String name, Double damage) {
        this.id = id;
        this.name = name;
        this.damage = damage;
    }

    public CardRequestModel() {

    }
}
