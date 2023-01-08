package julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.User;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighterResult;

public interface IMessageFactory {
    String createCardFightMsg(User firstPlayer, ICard firstCard, User secondPlayer, ICard secondCard, CardFighterResult fightResult);

    String createUserErrorMsg(User user, String msg);

    String createRoundLimitMsg();

    String createEndGameMsg(User winner);
}
