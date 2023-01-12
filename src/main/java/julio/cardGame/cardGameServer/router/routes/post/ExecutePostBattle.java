package julio.cardGame.cardGameServer.router.routes.post;

import julio.cardGame.cardGameServer.http.HttpServer;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.AuthenticatedRoute;
import julio.cardGame.cardGameServer.router.Routeable;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.cardGameServer.application.dbLogic.models.UserInfoModel;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

public class ExecutePostBattle extends AuthenticatedRoute implements Routeable, Observer {

    private boolean listChanged = false;
    @Override
    public Response process(RequestContext requestContext) {

        HttpServer.battleRes.addObserver(this);

        //todo fetch data from db and put into queue

        HttpServer.battleRes.addUserQueue(new UserInfoModel("julio", UUID.fromString("6cd85277-4590-49d4-b0cf-ba0a921faad0"), 800));

        //waits until the battleResults are changed
        while (!listChanged) {

        }

        HttpServer.battleRes.deleteObserver(this);

        String body = "";

        for (String line : this.battleRes) {
            body += line + "/n";
        }

        //if there is only one message it is an error
        if (this.battleRes.size() == 1) {

            return new Response(body, HttpStatus.INTERNAL_SERVER_ERROR);

        } else {

            return new Response(body, HttpStatus.OK);

        }

        //here we have the list
        //return null;
    }

    @Override
    public void update(Observable observable, Object o) {
        setBattleRes((List<String>) o);
        this.listChanged = true;
    }

    public List<String> getBattleRes() {
        return battleRes;
    }

    public void setBattleRes(List<String> battleRes) {
        this.battleRes = battleRes;
    }

    private List<String> battleRes;
}
