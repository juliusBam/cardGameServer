package julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.BattleUser;

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
