package julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards;


import julio.cardGame.cardGameServer.application.battleLogic.gameParts.BattleUser;

public interface ICard {
    void trade(BattleUser targetPlayer);
    void move(Deck targetDeck);
    int fight(ICard cardToFight);

    String getName();
}

