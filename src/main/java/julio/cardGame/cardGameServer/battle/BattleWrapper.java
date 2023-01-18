package julio.cardGame.cardGameServer.battle;

import julio.cardGame.cardGameServer.battle.CardGame;
import julio.cardGame.cardGameServer.database.models.UserInfoModel;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class BattleWrapper {

    public List<String> getBattleResult() {
        return battleResult;
    }

    protected PropertyChangeSupport propertyChangeSupport;
    private List<String> battleResult;

    //TODO implement observer pattern from "scratch" -> implement observable and observer
    public CompletableFuture<List<String>> completableBattleRes = new CompletableFuture<>();
    private BlockingQueue<UserInfoModel> battleQueue = new ArrayBlockingQueue<>(2);

    public BattleWrapper() {

        this.propertyChangeSupport = new PropertyChangeSupport(this);

    }

    public void updateBattleResult(List<String> newResults) {

        List<String> oldValue = this.battleResult;
        //List<String> oldValue = Collections.synchronizedList(new ArrayList<>());
        //oldValue.add("");
        this.battleResult = Collections.synchronizedList(new ArrayList<>(newResults));
        //this.battleResult = Collections.synchronizedList(new ArrayList<>(newResults));
                //new ArrayList<>(newResults);

        this.propertyChangeSupport.firePropertyChange("Battle res", oldValue, this.battleResult);
        //notifyObservers(newResults);

        //this.propertyChangeSupport.firePropertyChange("Battle res", oldValue, this.battleResult);
    }

    public void addUserQueue(UserInfoModel newUser) {
        this.battleQueue.add(newUser);
        if (this.battleQueue.size() == 2) {

            this.play();

        }
    }

    public void play() {

        List<String> battleRes;

        try {

            CardGame game = new CardGame(battleQueue.take(), battleQueue.take());
            battleRes = game.playRound();

        } catch (InterruptedException e) {

            battleRes = new ArrayList<>();
            battleRes.add(DefaultMessages.ERR_USERS_BATTLE_QUEUE.getMessage());

        }

        this.updateBattleResult(battleRes);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {

        this.propertyChangeSupport.addPropertyChangeListener(listener);

    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {

        this.propertyChangeSupport.removePropertyChangeListener(listener);

    }

}
