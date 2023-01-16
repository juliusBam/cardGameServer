package julio.cardGame.cardGameServer.http;

import julio.cardGame.cardGameServer.application.battleLogic.gameParts.CardGame;
import julio.cardGame.cardGameServer.application.dbLogic.BattleExecutor;
import julio.cardGame.cardGameServer.application.dbLogic.models.UserInfoModel;
import julio.cardGame.common.DefaultMessages;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BattleWrapper {

    public static List<String> getBattleResult() {
        return battleResult;
    }

    //protected static PropertyChangeSupport propertyChangeSupport;
    private static List<String> battleResult;

    public static CompletableFuture<List<String>> completableBattleRes = new CompletableFuture<>();
    private static BlockingQueue<UserInfoModel> battleQueue = new ArrayBlockingQueue<>(2);

    private static boolean resultChanged = false;

    BattleWrapper() {

        //BattleWrapper.propertyChangeSupport = new PropertyChangeSupport(this);

    }

    public static void updateBattleResult(List<String> newResults) {

        //List<String> oldValue = this.battleResult;
        //List<String> oldValue = Collections.synchronizedList(new ArrayList<>());
        //oldValue.add("");
        BattleWrapper.battleResult = Collections.synchronizedList(new ArrayList<>(newResults));
        BattleWrapper.resultChanged = true;
        //this.battleResult = Collections.synchronizedList(new ArrayList<>(newResults));
                //new ArrayList<>(newResults);

        //this.propertyChangeSupport.firePropertyChange("Battle res", oldValue, this.battleResult);
        //notifyObservers(newResults);

        //this.propertyChangeSupport.firePropertyChange("Battle res", oldValue, this.battleResult);
    }

    public static void addUserQueue(UserInfoModel newUser) {
        battleQueue.add(newUser);
        if (battleQueue.size() == 2) {

            playBattle();

        }
    }

    private static void playBattle() {

        List<String> battleResult;

        try {

            CardGame game = new CardGame(battleQueue.take(), battleQueue.take());
            battleResult = game.playRound();


        } catch (InterruptedException e) {

            battleResult = new ArrayList<>();
            battleResult.add(DefaultMessages.ERR_USERS_BATTLE_QUEUE.getMessage());

        }

        updateBattleResult(battleResult);

    }

    /*public void addPropertyChangeListener(PropertyChangeListener listener) {

        this.propertyChangeSupport.addPropertyChangeListener(listener);

    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {

        this.propertyChangeSupport.removePropertyChangeListener(listener);

    }*/

    public static void removePlayerFromQueue() {
        battleQueue.remove();
        if (battleQueue.size() == 0) {
            battleQueue = new ArrayBlockingQueue<>(2);
            resultChanged = false;
        }
    }
    public static boolean getResultChanged() {
        return resultChanged;
    }

}
