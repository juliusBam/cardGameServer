package julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.BattleUser;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighterResult;

public class MessageFactory implements IMessageFactory {

    //todo refactor param
    public String createCardFightMsg(BattleUser firstPlayer, ICard firstCard, BattleUser secondPlayer, ICard secondCard, CardFighterResult fightResult) {

        //in the case we have a draw

        String firstBaseMsg = "%s: %s (%s) vs ";
        String secondBaseMsg = "%s: %s (%s)";

        String formattedFirstMsg = String.format(firstBaseMsg, firstPlayer.getName(), firstCard.getName(), Integer.toString(fightResult.getFirstCardDmg()));
        String formattedSecondMsg = String.format(secondBaseMsg, secondPlayer.getName(), secondCard.getName(), Integer.toString(fightResult.getSecondCardDmg()));

        if (fightResult.getLoser() == null && fightResult.getWinner() == null) {
            return createFightDrawMsg(formattedFirstMsg, formattedSecondMsg);
        }

        String lastMsgPart = " => %s %s %s";
        String formattedLastMsg = String.format(lastMsgPart, fightResult.getWinner().getName(), " kills ", fightResult.getLoser().getName());

        String totalMsg = formattedFirstMsg.concat(formattedSecondMsg).concat(formattedLastMsg);
        return totalMsg;
    }

    public String createUserErrorMsg(BattleUser user, String msg) {

        return "";
    }

    private String createFightDrawMsg(String firstPart, String secondPart) {
        return firstPart.concat(secondPart).concat(" => had the same damage, the match is a draw");
    }

    public String createRoundLimitMsg() {
        return "The round limit was reached";
    }

    public String createEndGameMsg(BattleUser winner) {
        if (winner == null) {
            return "The game was a draw";
        } else {
            return "Player " + winner.getName() + "won";
        }
    }
}
