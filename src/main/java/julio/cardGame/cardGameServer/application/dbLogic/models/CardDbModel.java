package julio.cardGame.cardGameServer.application.dbLogic.models;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.Elements;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.monsters.Races;
import julio.cardGame.common.CardTypes;

import java.util.UUID;

public class CardDbModel {
    public UUID cardID;

    public UUID deckID;

    public UUID ownerID;

    public String cardName;

    public double card_damage;

    public String cardElement;

    public String cardType;

    public String monsterRace;

    public CardDbModel(CardRequestModel requestModel) {

        this.cardID = requestModel.id;
        this.card_damage = requestModel.damage;
        this.cardName = requestModel.name;
        this.parseName(requestModel.name);

    }

    public CardDbModel(UUID cardID, String cardName, double card_damage, String cardElement, String cardType, String monsterRace) {
        this.cardID = cardID;
        this.cardName = cardName;
        this.card_damage = card_damage;
        this.cardElement = cardElement;
        this.cardType = cardType;
        this.monsterRace = monsterRace;
    }

    public CardDbModel(UUID cardID, UUID deckID, UUID ownerID, String cardName, double card_damage, String cardElement, String cardType, String monsterRace) {
        this.cardID = cardID;
        this.deckID = deckID;
        this.ownerID = ownerID;
        this.cardName = cardName;
        this.card_damage = card_damage;
        this.cardElement = cardElement;
        this.cardType = cardType;
        this.monsterRace = monsterRace;
    }

    private void parseName(String cardName) {
        this.cardElement = extractElement(cardName);
        this.monsterRace = extractRace(cardName);
        this.cardType = extractType(cardName);
    }

    private String extractRace(String cardName) {

        if (cardName.contains(Races.Elf.getRace()))
            return Races.Elf.getRace();

        if (cardName.contains(Races.Goblin.getRace()))
            return Races.Goblin.getRace();

        if (cardName.contains(Races.Kraken.getRace()))
            return Races.Kraken.getRace();

        if (cardName.contains(Races.Wizard.getRace()))
            return Races.Wizard.getRace();

        if (cardName.contains(Races.Ork.getRace()))
            return Races.Ork.getRace();

        if (cardName.contains(Races.Knight.getRace()))
            return Races.Knight.getRace();

        if (cardName.contains(Races.Troll.getRace()))
            return Races.Troll.getRace();

        if (cardName.contains(Races.Dragon.getRace()))
            return Races.Dragon.getRace();

        return null;
    }

    private String extractElement(String cardName) {

        if (cardName.contains(Elements.Fire.getElement())) {
            return Elements.Fire.getElement();
        }

        if (cardName.contains(Elements.Water.getElement())) {
            return Elements.Water.getElement();
        }

        return Elements.Normal.getElement();

    }

    private String extractType(String cardName) {

        if (cardName.contains(CardTypes.SPELL.getCardType()) || this.monsterRace == null) {
            return CardTypes.SPELL.getCardType();
        }

        return CardTypes.MONSTER.getCardType();

    }
}
