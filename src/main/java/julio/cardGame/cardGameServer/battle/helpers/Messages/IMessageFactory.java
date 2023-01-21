package julio.cardGame.cardGameServer.battle.helpers.Messages;

import julio.cardGame.cardGameServer.battle.BattleUser;
import julio.cardGame.cardGameServer.battle.cards.ICard;
import julio.cardGame.cardGameServer.battle.helpers.Fighter.CardFighterResult;

public interface IMessageFactory {
    String createCardFightMsg(BattleUser firstPlayer, ICard firstCard, BattleUser secondPlayer, ICard secondCard, CardFighterResult fightResult);

    String createRoundLimitMsg();

    String createEndGameMsg(BattleUser winner);
}
