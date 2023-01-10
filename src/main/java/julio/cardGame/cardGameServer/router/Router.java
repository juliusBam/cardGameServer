package julio.cardGame.cardGameServer.router;

import julio.cardGame.cardGameServer.http.HttpPath;
import julio.cardGame.cardGameServer.http.HttpVerb;
import julio.cardGame.cardGameServer.router.routes.delete.ExecuteDeleteTrading;
import julio.cardGame.cardGameServer.router.routes.get.*;
import julio.cardGame.cardGameServer.router.routes.post.*;
import julio.cardGame.cardGameServer.router.routes.put.ExecutePutDeck;
import julio.cardGame.cardGameServer.router.routes.put.ExecutePutUser;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<RouteIdentifier, Routeable> routes = new HashMap<>();

    public void registerRoute(RouteIdentifier routeIdentifier, Routeable routeable) {
        routes.put(routeIdentifier, routeable);
    }

    public Routeable findRoute(RouteIdentifier routeIdentifier) {
        return routes.get(routeIdentifier);
    }

    public Router() {

        //we register the user routes
        registerRoute(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.POST.getVerb()), new ExecutePostUser());

        registerRoute(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetUser());

        registerRoute(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.PUT.getVerb()), new ExecutePutUser());

        //we register the session routes
        registerRoute(new RouteIdentifier(HttpPath.SESSIONS.getPath(), HttpVerb.POST.getVerb()), new ExecutePostSession());

        //we register the packages routes
        registerRoute(new RouteIdentifier(HttpPath.PACKAGES.getPath(), HttpVerb.POST.getVerb()), new ExecutePostPackage());

        //we register the transactions/packages route
        registerRoute(new RouteIdentifier(HttpPath.TRANSACTIONS_PACKAGES.getPath(), HttpVerb.POST.getVerb()), new ExecutePostTransactionPackage());

        //we register the tradings route
        registerRoute(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetTrading());

        registerRoute(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.POST.getVerb()), new ExecutePostTrading());

        registerRoute(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.DELETE.getVerb()), new ExecuteDeleteTrading());

        //we register the cards route
        registerRoute(new RouteIdentifier(HttpPath.CARDS.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetCard());

        //we register the decks route
        registerRoute(new RouteIdentifier(HttpPath.DECK.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetDeck());

        registerRoute(new RouteIdentifier(HttpPath.DECK.getPath(), HttpVerb.PUT.getVerb()), new ExecutePutDeck());

        //we register the stats route
        registerRoute(new RouteIdentifier(HttpPath.STATS.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetStats());

        //we register the score route
        registerRoute(new RouteIdentifier(HttpPath.SCORE.getPath(), HttpVerb.GET.getVerb()), new ExecuteGetScore());

        //we register the battles route
        registerRoute(new RouteIdentifier(HttpPath.BATTLES.getPath(), HttpVerb.POST.getVerb()), new ExecutePostBattle());



    }

}
