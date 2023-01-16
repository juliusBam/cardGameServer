package julio.cardGame.cardGameServer.application.battleLogic.gameParts;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.cards.ICard;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighter;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Fighter.CardFighterResult;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages.IMessageFactory;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.Messages.MessageFactory;
import julio.cardGame.cardGameServer.application.battleLogic.gameParts.helpers.UserBattleResultWrapper;
import julio.cardGame.cardGameServer.application.dbLogic.db.DbConnection;
import julio.cardGame.cardGameServer.application.dbLogic.models.UserInfoModel;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;
import julio.cardGame.common.DefaultMessages;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardGame {
    public int roundCounter = 0;

    //players are final
    private final BattleUser firstPlayer;
    private final BattleUser secondPlayer;

    public CardGame(UserInfoModel firstUser, UserInfoModel secondUser) {

        this.firstPlayer = new BattleUser(firstUser);

        //todo fetches the owned cards from the db
        //this.firstPlayer.createCollection();

        this.secondPlayer = new BattleUser(secondUser);
        //todo fetches the owned cards from the db
        //this.secondPlayer.createCollection();

    }

    public void createDecks() throws SQLException {

        this.firstPlayer.createDeck();
        this.secondPlayer.createDeck();

    }

    public List<String> playRound() {

        IMessageFactory messageFactory = new MessageFactory();

        CardFighter cardFighter = new CardFighter();

        List<String> battleResult = new ArrayList<>();

        try {

            //players create their decks
            this.createDecks();

            while (this.playersHaveCards()) {

                if (this.roundCounter >= 100) {
                    System.out.println(messageFactory.createRoundLimitMsg());
                    break;
                }
                this.roundCounter++;

                    final ICard firstPlayerFirstCard = firstPlayer.getFirstCard();
                    final ICard secondPlayerSecondCard = secondPlayer.getFirstCard();

                    CardFighterResult fightResult = cardFighter.returnFightResults(firstPlayerFirstCard, secondPlayerSecondCard);

                    if (fightResult == null) {
                        throw new RuntimeException("Fight result is null");
                    } else {

                        battleResult.add(
                                messageFactory
                                        .createCardFightMsg(this.firstPlayer, firstPlayerFirstCard, this.secondPlayer, secondPlayerSecondCard, fightResult));

                        //if round was not a draw move the loser
                        if (fightResult.getLoser() != null) {

                            if (fightResult.getLoser().equals(firstPlayerFirstCard)) {

                                this.firstPlayer.moveCardTo(this.secondPlayer);

                            } else {

                                this.secondPlayer.moveCardTo(this.firstPlayer);

                            }

                        } else {

                            this.handleDraw();

                        }

                    }

            }

            UserBattleResultWrapper battleResultWrapper = this.calculateWinner();

            //if it is not a draw we update the stats
            if (!battleResultWrapper.draw) {

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

    public BattleUser getFirstPlayer() {
        return firstPlayer;
    }

    public BattleUser getSecondPlayer() {
        return secondPlayer;
    }

    private boolean playersHaveCards() {
        return this.firstPlayer.hasCards() && this.secondPlayer.hasCards();
    }

    private UserBattleResultWrapper calculateWinner() {
        int firstPlayerCards = this.firstPlayer.getDeckSize();
        int secondPlayerCards = this.secondPlayer.getDeckSize();

        if (firstPlayerCards == secondPlayerCards) {
            return new UserBattleResultWrapper(null, null, true);
        } else {

            if (firstPlayerCards > secondPlayerCards)
                return new UserBattleResultWrapper(firstPlayer, secondPlayer, false);

            return new UserBattleResultWrapper(secondPlayer, firstPlayer, false);

            //return firstPlayerCards > secondPlayerCards ? this.firstPlayer : this.secondPlayer;
        }
    }

}

