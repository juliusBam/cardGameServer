package julio.cardGame.cardGameServer.battle.helpers.Messages;

import julio.cardGame.cardGameServer.Constants;
import julio.cardGame.cardGameServer.battle.BattleUser;
import julio.cardGame.cardGameServer.battle.cards.ICard;
import julio.cardGame.cardGameServer.battle.helpers.Fighter.CardFighterResult;

public class MessageFactory implements IMessageFactory {

    public String createCardFightMsg(BattleUser firstPlayer, ICard firstCard, BattleUser secondPlayer, ICard secondCard, CardFighterResult fightResult) {

        String firstBaseMsg = "%s: %s (%s) vs ";
        String secondBaseMsg = "%s: %s (%s)";

        String formattedFirstMsg = String.format(firstBaseMsg, firstPlayer.getName(), firstCard.getName(), fightResult.getFirstCardDmg());
        String formattedSecondMsg = String.format(secondBaseMsg, secondPlayer.getName(), secondCard.getName(), fightResult.getSecondCardDmg());

        if (fightResult.getLoser() == null && fightResult.getWinner() == null) {
            return createFightDrawMsg(formattedFirstMsg, formattedSecondMsg);
        }

        String formattedLastMsg = "";

        if (fightResult.getFirstCardDmg() == Constants.WATERSPELL_VS_KNIGHT || fightResult.getSecondCardDmg() == Constants.WATERSPELL_VS_KNIGHT) {

            formattedLastMsg = "=> The knight drowned";

        } else {
            String lastMsgPart = " => %s %s %s";
            formattedLastMsg = String.format(lastMsgPart, fightResult.getWinner().getName(), " kills ", fightResult.getLoser().getName());
        }

        if (fightResult.isRickRoll()) {

            formattedLastMsg = formattedLastMsg.concat( " ||| Rickrolled! |||");

        }

        return formattedFirstMsg.concat(formattedSecondMsg).concat(formattedLastMsg);

    }

    private String createFightDrawMsg(String firstPart, String secondPart) {

        return firstPart.concat(secondPart).concat(" => had the same damage, the match is a draw");

    }

    public String createRoundLimitMsg() {
        return "The round limit was reached";
    }

    public String createEndGameMsg(BattleUser winner) {

        final String endGameMsg = "Player %s won";

        if (winner == null) {
            return "The game was a draw";
        } else {
            return String.format(endGameMsg, winner.getName());
        }

    }
}
