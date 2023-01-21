package julio.cardGame.cardGameServer.services.battleServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import julio.cardGame.cardGameServer.battle.BattleResultSubscriber;
import julio.cardGame.cardGameServer.controllers.AuthenticationController;
import julio.cardGame.cardGameServer.models.CompleteUserModel;
import julio.cardGame.cardGameServer.models.UserInfoModel;
import julio.cardGame.cardGameServer.database.repositories.UserRepo;
import julio.cardGame.cardGameServer.http.HttpServer;
import julio.cardGame.cardGameServer.http.communication.HttpStatus;
import julio.cardGame.cardGameServer.http.communication.RequestContext;
import julio.cardGame.cardGameServer.http.communication.Response;
import julio.cardGame.cardGameServer.http.routing.AuthorizationWrapper;
import julio.cardGame.cardGameServer.services.CardGameService;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class PostBattleService implements CardGameService, BattleResultSubscriber {

    private boolean listChanged = false;

    private List<String> battleRes;

    public void setBattleRes(List<String> battleRes) {
        this.battleRes = battleRes;
        this.listChanged = true;
    }

    @Override
    public Response execute(RequestContext requestContext, AuthorizationWrapper authorizationWrapper) throws JsonProcessingException, SQLException, NoSuchAlgorithmException {

        try {

            AuthorizationWrapper auth = AuthenticationController.requireAuthToken(requestContext.getHeaders());

            if (auth.response != null)
                return auth.response;

            UserRepo userRepo = new UserRepo();

            //Create the user data for the battle
            CompleteUserModel userData = userRepo.getUser(auth.userName);

            //subscribe to the battle result provider
            HttpServer.battleWrapper.subscribe(this);

            //add the user into the queue
            HttpServer.battleWrapper.addUserQueue(
                    new UserInfoModel(userData.userName, userData.userStatsModel.elo)
            );

            //waits until the battleResults are changed
            while (!this.listChanged) {
                Thread.sleep(50);
            }

            String body = "";

            for (String line : this.battleRes) {
                body += line + System.lineSeparator();
            }

            //if there is only one message it is an error
            if (this.battleRes.size() == 1) {

                return new Response(body, HttpStatus.INTERNAL_SERVER_ERROR);

            } else {

                return new Response(body, HttpStatus.OK);

            }

        } catch (InterruptedException e) {
            return new Response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public void update() {
        setBattleRes(HttpServer.battleWrapper.getResult());
        this.listChanged = true;
        HttpServer.battleWrapper.unsubscribe(this);
    }
}
