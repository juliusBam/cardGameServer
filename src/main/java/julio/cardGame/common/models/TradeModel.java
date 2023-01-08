package julio.cardGame.common.models;

import java.util.UUID;

public class TradeModel {
    public UUID Id;

    public UUID CardToTrade;

    public String Type;

    public int MinimumDamage;


    public TradeModel(UUID id, UUID cardToTrade, String type, int minimumDamage) {
        Id = id;
        CardToTrade = cardToTrade;
        Type = type;
        MinimumDamage = minimumDamage;
    }

    public TradeModel(){}
}
