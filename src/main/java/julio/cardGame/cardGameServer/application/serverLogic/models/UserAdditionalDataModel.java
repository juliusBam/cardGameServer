package julio.cardGame.cardGameServer.application.serverLogic.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAdditionalDataModel {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Bio")
    public String bio;
    @JsonProperty("Image")
    public String image;

    public UserAdditionalDataModel(String name, String bio, String image) {
        this.name = name;
        this.bio = bio;
        this.image = image;
    }

    public UserAdditionalDataModel(){}
}
