package julio.cardGame.cardGameServer.router.routes.post;

import julio.cardGame.cardGameServer.application.dbLogic.models.CompleteUserModel;
import julio.cardGame.cardGameServer.application.dbLogic.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.BattleWrapper;
import julio.cardGame.cardGameServer.http.HttpServer;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.AuthorizationWrapper;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.cardGameServer.application.dbLogic.models.UserInfoModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

public class ExecutePostBattle extends AuthenticatedRoute implements Routeable, PropertyChangeListener, Observer {

    private boolean listChanged = false;
    @Override
    public Response process(RequestContext requestContext) {

        try {

            AuthorizationWrapper auth = this.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            CompleteUserModel userData = new UserRepo().getUser(auth.userName);

            /*HttpServer.battleRes.addUserQueue(
                    new UserInfoModel(userData.userName, userData.userStatsModel.elo)
            );

            HttpServer.battleRes.addObserver(this);*/

            //HttpServer.battleWrapper.addPropertyChangeListener(this);

            BattleWrapper.addUserQueue(
                    new UserInfoModel(userData.userName, userData.userStatsModel.elo)
            );

            System.out.println(Thread.currentThread().getName() + " waiting for list change");

            //waits until the battleResults are changed
            /*while (HttpServer.getResultChanged()) {

            }*/

            //listener not needed anymore
            //HttpServer.battleWrapper.removePropertyChangeListener(this);

            //HttpServer.battleRes.deleteObserver(this);

            String body = "";

            for (String line : BattleWrapper.getBattleResult()) {
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
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        //here we have the list
        //return null;
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
