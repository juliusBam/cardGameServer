package julio.cardGame.cardGameServer.battle;

import julio.cardGame.cardGameServer.models.UserInfoModel;
import julio.cardGame.cardGameServer.http.communication.DefaultMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BattleWrapper implements BattleResultProvider {
    private List<String> battleResult;

    private List<BattleResultSubscriber> subscribers = new ArrayList<>();
    private BlockingQueue<UserInfoModel> battleQueue = new ArrayBlockingQueue<>(2);

    public BattleWrapper() {

    }

    public void addUserQueue(UserInfoModel newUser) {
        this.battleQueue.add(newUser);
        if (this.battleQueue.size() == 2) {

            this.play();

        }
    }

    public void play() {

        List<String> newBattleRes;

        try {

            CardGame game = new CardGame(battleQueue.take(), battleQueue.take());
            newBattleRes = game.executeCardBattle();

        } catch (InterruptedException e) {

            newBattleRes = new ArrayList<>();
            newBattleRes.add(DefaultMessages.ERR_USERS_BATTLE_QUEUE.getMessage());

        }

        this.updateResult(newBattleRes);

    }

    @Override
    public void subscribe(BattleResultSubscriber battleResultSubscriber) {

        subscribers.add(battleResultSubscriber);

    }

    @Override
    public void unsubscribe(BattleResultSubscriber battleResultSubscriber) {

        subscribers.remove(battleResultSubscriber);

    }

    @Override
    public void notifySubscribers() {

        //we iterate the subscribers in a reverse order, since the thread running this operation
        //is in the first position, if we update its battle Service the loop will terminate
        //not notifying the other thread
        for (int i = this.subscribers.size() - 1; i > -1; i--) {

            subscribers.get(i).update();

        }

    }

    @Override
    public void updateResult(List<String> newResult) {

        this.battleResult = newResult;

        this.notifySubscribers();

    }

    @Override
    public List<String> getResult() {
        return this.battleResult;
    }
}
