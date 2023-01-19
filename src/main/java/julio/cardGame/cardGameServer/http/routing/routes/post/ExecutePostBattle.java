package julio.cardGame.cardGameServer.http.routing.routes.post;

import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.database.models.CompleteUserModel;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.HttpServer;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.routes.AuthenticatedRoute;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.http.routing.routes.Routeable;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.database.models.UserInfoModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

public class ExecutePostBattle implements Routeable, PropertyChangeListener, Observer {

    private boolean listChanged = false;

    private final UserRepo userRepo;

    public ExecutePostBattle() {
        this.userRepo = new UserRepo();
    }

    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = AuthenticationController.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            CompleteUserModel userData = this.userRepo.getUser(auth.userName);

            HttpServer.battleWrapper.addPropertyChangeListener(this);

            HttpServer.battleWrapper.addUserQueue(
                    new UserInfoModel(userData.userName, userData.userStatsModel.elo)
            );

            //waits until the battleResults are changed
            while (!this.listChanged) {
                Thread.sleep(50);
            }

            //HttpServer.battleRes.deleteObserver(this);

            String body = "";

            //System.out.println(Thread.currentThread().getName() + " waiting for list change");
            HttpServer.battleWrapper.removePropertyChangeListener(this);
            //HttpServer.battleWrapper.removePlayerFromQueue();
            //List<String> battleRes = BattleWrapper.completableBattleRes.join();

            //System.out.println(Thread.currentThread().getName() + " has changed list");
            for (String line : this.battleRes) {
                body += line + System.lineSeparator();
            }

            //System.out.println(Thread.currentThread().getName() + " will send response");

            //if there is only one message it is an error
            if (this.battleRes.size() == 1) {

                return new Response(body, HttpStatus.INTERNAL_SERVER_ERROR);

            } else {

                return new Response(body, HttpStatus.OK);

            }

        } catch (SQLException e) {

            return new Response(e);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Observable observable, Object object) {

        System.out.println(Thread.currentThread().getName() + " notified of the change");
        setBattleRes(new ArrayList<>((List<String>) object));
        this.listChanged = true;

    }

    public List<String> getBattleRes() {
        return battleRes;
    }

    public void setBattleRes(List<String> battleRes) {
        this.battleRes = battleRes;
        this.listChanged = true;
    }

    private List<String> battleRes;

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

        System.out.println(Thread.currentThread().getName() + " notified of the change");
        /*setBattleRes(new ArrayList<>((List<String>) object));
        this.listChanged = true;*/
        if (propertyChangeEvent.getPropertyName().equals("Battle res")) {

            setBattleRes((List<String>) propertyChangeEvent.getNewValue());
            //System.out.println(propertyChangeEvent.getNewValue().toString());
        }

    }

    public class ResultAwait implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

            System.out.println(Thread.currentThread().getName() + " notified of the change");
        /*setBattleRes(new ArrayList<>((List<String>) object));
        this.listChanged = true;*/
            if (propertyChangeEvent.getPropertyName().equals("Battle res")) {

                setBattleRes((List<String>) propertyChangeEvent.getNewValue());
                System.out.println(propertyChangeEvent.getNewValue().toString());
            }

        }

    }

}
