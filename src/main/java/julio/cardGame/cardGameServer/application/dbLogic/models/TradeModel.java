package julio.cardGame.cardGameServer.application.dbLogic.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TradeModel {
    @JsonProperty("Id")
    public UUID id;

    @JsonProperty("CardToTrade")
    public UUID cardToTrade;

    @JsonProperty("Type")
    public String type;

    @JsonProperty("MinimumDamage")
    public int minimumDamage;


    public TradeModel(UUID id, UUID cardToTrade, String type, int minimumDamage) {
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = type;
        this.minimumDamage = minimumDamage;
    }

    public TradeModel(){}
}
