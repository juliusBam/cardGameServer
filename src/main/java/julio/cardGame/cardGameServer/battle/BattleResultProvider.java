package julio.cardGame.cardGameServer.battle;

import java.util.List;

public interface BattleResultProvider {

    void subscribe(BattleResultSubscriber battleResultSubscriber);

    void unsubscribe(BattleResultSubscriber battleResultSubscriber);

    void notifySubscribers();

    void updateResult(List<String> newResult);

    List<String> getResult();

}
