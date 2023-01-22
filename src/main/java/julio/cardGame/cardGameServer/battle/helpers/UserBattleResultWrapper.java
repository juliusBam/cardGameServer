package julio.cardGame.cardGameServer.battle.helpers;

import julio.cardGame.cardGameServer.battle.BattleUser;

//Encapsulates the results of the battle

public class UserBattleResultWrapper {

    public BattleUser winner;

    public BattleUser loser;

    public boolean draw;

    public UserBattleResultWrapper(BattleUser winner, BattleUser loser, boolean draw) {
        this.winner = winner;
        this.loser = loser;
        this.draw = draw;
    }
}
