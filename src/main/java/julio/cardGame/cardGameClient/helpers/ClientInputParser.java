package julio.cardGame.cardGameClient.helpers;

import julio.cardGame.cardGameClient.services.Actions.*;
import julio.cardGame.cardGameClient.services.Actions.deleteActions.DeleteTradeAction;
import julio.cardGame.cardGameClient.services.Actions.getActions.*;
import julio.cardGame.cardGameClient.services.Actions.postActions.*;
import julio.cardGame.cardGameClient.services.Actions.putActions.CreateDeckAction;
import julio.cardGame.cardGameClient.services.Actions.putActions.UpdateUserAction;

public class ClientInputParser {
    public Action parseInput(String userInput) {

        if (userInput.equals(ClientAction.QUIT.getClientAction())) {
            return new QuitAction();
        }
        if (userInput.equals(ClientAction.SHOW_CARDS.getClientAction())) {
            return new FetchCardsAction();
        }
        if (userInput.equals(ClientAction.TRADE.getClientAction())) {
            return new TradeAction();
        }
        if (userInput.equals(ClientAction.BUY_PACKAGE.getClientAction())) {
            return new BuyPackageAction();
        }
        if (userInput.equals(ClientAction.LOGIN.getClientAction())) {
            return new LoginAction();
        }
        if (userInput.equals(ClientAction.BATTLE.getClientAction())) {
            return new BattleAction();
        }
        if (userInput.equals(ClientAction.CREATE_USER.getClientAction())) {
            return new CreateUserAction();
        }
        if (userInput.equals(ClientAction.CREATE_PACKAGES.getClientAction())) {
            return new CreatePackageAction();
        }
        if (userInput.equals(ClientAction.SHOW_DECK.getClientAction())) {
            return new ShowDeckAction();
        }
        if (userInput.equals(ClientAction.CREATE_DECK.getClientAction())) {
            return new CreateDeckAction();
        }
        if (userInput.equals(ClientAction.UPDATE_USER.getClientAction())) {
            return new UpdateUserAction();
        }
        if (userInput.equals(ClientAction.SHOW_USER.getClientAction())) {
            return new ShowUserAction();
        }
        if (userInput.equals(ClientAction.SHOW_STATS.getClientAction())) {
            return new ShowStatsAction();
        }
        if (userInput.equals(ClientAction.SHOW_SCORE.getClientAction())) {
            return new ShowScoreAction();
        }
        if (userInput.equals(ClientAction.SHOW_TRADES.getClientAction())) {
            return new ShowTradesAction();
        }
        if (userInput.equals(ClientAction.POST_TRADE.getClientAction())) {
            return new PostTradeAction();
        }
        if (userInput.equals(ClientAction.DELETE_TRADE_DEAL.getClientAction())) {
            return new DeleteTradeAction();
        }

        return null;
    }

}
