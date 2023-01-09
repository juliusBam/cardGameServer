package julio.cardGame.cardGameServer.router.routes.post;

import julio.cardGame.cardGameServer.http.HttpServer;
import julio.cardGame.cardGameServer.http.RequestContext;
import julio.cardGame.cardGameServer.http.Response;
import julio.cardGame.cardGameServer.router.Route;
import julio.cardGame.common.HttpStatus;
import julio.cardGame.common.models.UserInfo;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

public class ExecutePostBattle implements Route, Observer {

    private boolean listChanged = false;
    @Override
    public Response process(RequestContext requestContext) {

        HttpServer.battleRes.addObserver(this);

        HttpServer.battleRes.addUserQueue(new UserInfo("julio", UUID.fromString("6cd85277-4590-49d4-b0cf-ba0a921faad0"), 800));

        while (!listChanged) {

        }

        //System.out.println(this.battleRes);
        //System.out.println(this.battleRes);

        String body = "";

        for (String line : this.battleRes) {
            body += line + "/n";
        }

        return new Response(body, HttpStatus.OK);
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
        //todo send the response
    }

    private List<String> battleRes;
}
