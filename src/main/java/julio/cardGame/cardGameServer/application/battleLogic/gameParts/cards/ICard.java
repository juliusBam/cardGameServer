package julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards;


import julio.cardGame.cardGameServer.application.battleLogic.gameParts.User;

public interface ICard {
    void trade(User targetPlayer);
    void move(Deck targetDeck);
    int fight(ICard cardToFight);

    String getName();
}

