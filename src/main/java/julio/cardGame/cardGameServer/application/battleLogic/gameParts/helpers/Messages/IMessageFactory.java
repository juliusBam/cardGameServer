package julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.BattleUser;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighterResult;

public interface IMessageFactory {
    String createCardFightMsg(BattleUser firstPlayer, ICard firstCard, BattleUser secondPlayer, ICard secondCard, CardFighterResult fightResult);

    String createUserErrorMsg(BattleUser user, String msg);

    String createRoundLimitMsg();

    String createEndGameMsg(BattleUser winner);
}
