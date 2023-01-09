package julio.cardGame.cardGameServer.application.serverLogic;

import julio.cardGame.common.models.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BattleResultObs extends Observable {
    private static List<String> battleResult;

    private static BlockingQueue<UserInfo> battleQueue = new ArrayBlockingQueue<>(2);

    public BattleResultObs() {

    }

    public void updateBattleResult(List<String> newResults) {
        battleResult = new ArrayList<>(newResults);
        setChanged();
        notifyObservers(newResults);
    }

    public void addUserQueue(UserInfo newUser) {
        battleQueue.add(newUser);
        if (battleQueue.size() == 2) {

            List<String> newResult = new ArrayList<>();
            newResult.add("In the battle result");
            newResult.add("Second message in the battle res");
            newResult.add("Winner is julio");
            updateBattleResult(newResult);
            battleQueue = new ArrayBlockingQueue<>(2);
        }
    }
}
