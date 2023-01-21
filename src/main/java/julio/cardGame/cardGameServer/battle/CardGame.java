package julio.cardGame.cardGameServer.battle;

import julio.cardGame.cardGameServer.battle.cards.ICard;
import julio.cardGame.cardGameServer.battle.helpers.Fighter.CardFighter;
import julio.cardGame.cardGameServer.battle.helpers.Fighter.CardFighterResult;
import julio.cardGame.cardGameServer.battle.helpers.Messages.IMessageFactory;
import julio.cardGame.cardGameServer.battle.helpers.Messages.MessageFactory;
import julio.cardGame.cardGameServer.battle.helpers.UserBattleResultWrapper;
import julio.cardGame.cardGameServer.database.DbConnection;
import julio.cardGame.cardGameServer.models.UserInfoModel;
import julio.cardGame.cardGameServer.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardGame {
    public int roundCounter = 0;

    private final BattleUser firstPlayer;
    private final BattleUser secondPlayer;

    public CardGame(UserInfoModel firstUser, UserInfoModel secondUser) {

        this.firstPlayer = new BattleUser(firstUser);

        this.secondPlayer = new BattleUser(secondUser);

    }

    public void createDecks() throws SQLException {

        this.firstPlayer.createDeck();
        this.secondPlayer.createDeck();

    }

    public List<String> executeCardBattle() {

        IMessageFactory messageFactory = new MessageFactory();

        CardFighter cardFighter = new CardFighter();

        List<String> battleResult = new ArrayList<>();

        try {

            //players create their decks
            this.createDecks();

            this.executeCardBattle(messageFactory, cardFighter, battleResult);


            UserBattleResultWrapper battleResultWrapper = this.calculateWinner();

            //if it is not a draw we update the stats
            if (!battleResultWrapper.draw) {

                this.updateUserStats(battleResultWrapper);

            }

            battleResult.add(messageFactory
                    .createEndGameMsg(
                            battleResultWrapper.draw ? null : battleResultWrapper.winner
                    )
            );

            return battleResult;

        } catch (RuntimeException e) {

            List<String> error = new ArrayList<String>();
            error.add(DefaultMessages.ERR_BATTLE_RES_NULL.getMessage());

            return error;

        } catch (SQLException e) {

            List<String> error = new ArrayList<String>();
            error.add(e.getMessage());

            return error;

        }

    }

    private void handleDraw() {
        this.firstPlayer.putCardBack();
        this.secondPlayer.putCardBack();
    }

    private boolean playersHaveCards() {
        return this.firstPlayer.hasCards() && this.secondPlayer.hasCards();
    }

    private UserBattleResultWrapper calculateWinner() {
        int firstPlayerCards = this.firstPlayer.getDeckSize();
        int secondPlayerCards = this.secondPlayer.getDeckSize();

        if (firstPlayerCards != 0 && secondPlayerCards != 0) {
            return new UserBattleResultWrapper(null, null, true);
        } else {

            if (firstPlayerCards != 0)
                return new UserBattleResultWrapper(firstPlayer, secondPlayer, false);

            return new UserBattleResultWrapper(secondPlayer, firstPlayer, false);

        }
    }

    private void executeCardBattle(IMessageFactory messageFactory, CardFighter cardFighter, List<String> battleResult) {

        while (this.playersHaveCards()) {

            if (this.roundCounter >= 100) {
                break;
            }
            this.roundCounter++;

            final ICard firstPlayerFirstCard = firstPlayer.getFirstCard();
            final ICard secondPlayerSecondCard = secondPlayer.getFirstCard();

            CardFighterResult fightResult = cardFighter.returnFightResults(firstPlayerFirstCard, firstPlayer, secondPlayerSecondCard, secondPlayer);

            if (fightResult == null) {
                throw new RuntimeException("Fight result is null");
            } else {

                battleResult.add(
                        messageFactory
                                .createCardFightMsg(this.firstPlayer, firstPlayerFirstCard, this.secondPlayer, secondPlayerSecondCard, fightResult));

                //if round was not a draw move the loser
                this.handleFightEnd(fightResult, firstPlayerFirstCard);

            }
        }

    }

    private void updateUserStats(UserBattleResultWrapper battleResultWrapper) throws SQLException {

        UserRepo userRepo = new UserRepo();

        try (Connection dbConnection = DbConnection.getInstance().connect()) {

            try {

                dbConnection.setAutoCommit(false);
                //update the winner
                userRepo.updateWinsElo(
                        dbConnection,
                        battleResultWrapper.winner.getName(),
                        battleResultWrapper.winner.getInfo().elo,
                        battleResultWrapper.loser.getInfo().elo
                );

                //update the loser
                userRepo.updateLossesElo(
                        dbConnection,
                        battleResultWrapper.loser.getName(),
                        battleResultWrapper.loser.getInfo().elo,
                        battleResultWrapper.winner.getInfo().elo
                );

                dbConnection.commit();

            } catch (SQLException e) {
                //we need to catch the exception here to set the rollback
                dbConnection.rollback();
                throw e;
            }

        }

    }

    private void handleFightEnd(CardFighterResult fightResult, ICard firstPlayerFirstCard) {

        if (fightResult.getLoser() != null) {

            this.handleMoveCards(fightResult, firstPlayerFirstCard);

        } else {

            this.handleDraw();

        }

    }

    private void handleMoveCards(CardFighterResult fightResult, ICard firstPlayerFirstCard) {

        if (fightResult.getLoser().equals(firstPlayerFirstCard)) {

            //first we move the opponent's card to the bottom
            this.firstPlayer.moveCardTo(this.secondPlayer);
            //then we move the played card to the bottom
            this.secondPlayer.moveCardTo(this.secondPlayer);

        } else {

            this.secondPlayer.moveCardTo(this.firstPlayer);
            this.firstPlayer.moveCardTo(this.firstPlayer);

        }

    }

}

