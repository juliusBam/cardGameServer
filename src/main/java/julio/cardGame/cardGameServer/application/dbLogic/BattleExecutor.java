package julio.cardGame.cardGameServer.application.dbLogic;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.CardGame;
import julio.cardGame.cardGameServer.application.dbLogic.models.UserInfoModel;
import julio.cardGame.common.DefaultMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BattleExecutor extends Observable {
    private static List<String> battleResult;

    private static BlockingQueue<UserInfoModel> battleQueue = new ArrayBlockingQueue<>(2);

    public BattleExecutor() {

    }

    public void updateBattleResult(List<String> newResults) {
        battleResult = Collections.synchronizedList(new ArrayList<>(newResults));
        setChanged();
        notifyObservers(battleResult);
    }

    public void addUserQueue(UserInfoModel newUser) {
        battleQueue.add(newUser);
        if (battleQueue.size() == 2) {

            this.playBattle();

        }
    }

    private void playBattle() {

        List<String> battleResult;

        try {

            CardGame game = new CardGame(battleQueue.take(), battleQueue.take());
            battleResult = game.playRound();


        } catch (InterruptedException e) {

            battleResult = new ArrayList<>();
            battleResult.add(DefaultMessages.ERR_USERS_BATTLE_QUEUE.getMessage());

        }
        //todo execute game
        //todo update elo, wins, losses in the db
        updateBattleResult(battleResult);
        battleQueue = new ArrayBlockingQueue<>(2);

    }
}
