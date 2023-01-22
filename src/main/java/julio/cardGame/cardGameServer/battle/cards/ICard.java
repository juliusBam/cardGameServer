package julio.cardGame.cardGameServer.battle.cards;

public interface ICard {
    int fight(ICard cardToFight);

    int getDmg();

    String getName();
}

