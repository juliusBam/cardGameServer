package julio.cardGame.cardGameServer.battle.cards;


import julio.cardGame.cardGameServer.battle.BattleUser;

public interface ICard {
    void trade(BattleUser targetPlayer);
    void move(Deck targetDeck);
    int fight(ICard cardToFight);

    int getDmg();

    String getName();
}

