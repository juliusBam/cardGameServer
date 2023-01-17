package julio.cardGame.cardGameServer.battle.services;


import julio.cardGame.cardGameServer.battle.cards.CardCreationDataset;
import julio.cardGame.cardGameServer.battle.cards.Elements;
import julio.cardGame.cardGameServer.battle.cards.monsters.Races;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardsFetcher {

    static int counter;

    /*public List<CardCreationDataset> getUserCollection(String username) {

        //todo send request to server with username to fetch the collection
        
        ArrayList<CardCreationDataset> creationResponse = new ArrayList<>();
        
        if (username == "julio") {
            
            creationResponse.add(new CardCreationDataset("My dragon", Elements.Fire, 5, Races.Dragon));
            creationResponse.add(new CardCreationDataset("My ork", Elements.Water, 5, Races.Ork));
            creationResponse.add(new CardCreationDataset("My goblin", Elements.Normal, 10, Races.Goblin));
            creationResponse.add(new CardCreationDataset("My spell", Elements.Fire, 5, null));
        
        } else {
            creationResponse.add(new CardCreationDataset("My spell", Elements.Normal, 2, null));
            creationResponse.add(new CardCreationDataset("My dragon", Elements.Fire, 5, Races.Dragon));
            creationResponse.add(new CardCreationDataset("My Wizard", Elements.Water, 5, Races.Wizard));
            creationResponse.add(new CardCreationDataset("My goblin", Elements.Normal, 10, Races.Goblin));
        }
        
        return creationResponse;
    }*/


    public List<CardCreationDataset> getNewPackage() {

        //todo send request to server to fetch 5 random cards



        ArrayList<CardCreationDataset> creationResponse = new ArrayList<>();

        if (counter % 2 == 0) {

            creationResponse.add(new CardCreationDataset("My dragon", Elements.Fire, 5, Races.Dragon));
            creationResponse.add(new CardCreationDataset("My ork", Elements.Water, 5, Races.Ork));
            creationResponse.add(new CardCreationDataset("My goblin", Elements.Normal, 10, Races.Goblin));
            creationResponse.add(new CardCreationDataset("My spell", Elements.Fire, 5, null));
            creationResponse.add(new CardCreationDataset("My spell", Elements.Normal, 2, null));


        } else {

            creationResponse.add(new CardCreationDataset("My spell", Elements.Normal, 2, null));
            creationResponse.add(new CardCreationDataset("My dragon", Elements.Fire, 5, Races.Dragon));
            creationResponse.add(new CardCreationDataset("My Wizard", Elements.Water, 5, Races.Wizard));
            creationResponse.add(new CardCreationDataset("My goblin", Elements.Normal, 10, Races.Goblin));
            creationResponse.add(new CardCreationDataset("My spell", Elements.Normal, 2, null));

        }

        counter++;

        return creationResponse;

    }

    public List<CardCreationDataset> getDeck(UUID userID) {

        //todo fetch from db

        return null;
    }

}
