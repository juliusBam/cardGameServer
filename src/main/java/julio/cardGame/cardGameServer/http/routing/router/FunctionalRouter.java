package julio.cardGame.cardGameServer.http.routing.router;

import julio.cardGame.cardGameServer.http.routing.HttpPath;
import julio.cardGame.cardGameServer.http.routing.HttpVerb;
import julio.cardGame.cardGameServer.http.routing.routes.delete.ExecuteDeleteTrading;
import julio.cardGame.cardGameServer.http.routing.routes.get.*;
import julio.cardGame.cardGameServer.http.routing.routes.post.*;
import julio.cardGame.cardGameServer.http.routing.routes.put.ExecutePutActivate;
import julio.cardGame.cardGameServer.http.routing.routes.put.ExecutePutDeactivate;
import julio.cardGame.cardGameServer.http.routing.routes.put.ExecutePutDeck;
import julio.cardGame.cardGameServer.http.routing.routes.put.ExecutePutUser;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//TODO add new endpoint to activate/deactivate users -> service and repo as well (only admin)

public class FunctionalRouter {

        private final ConcurrentMap<RouteIdentifier, RouteEntry> routes = new ConcurrentHashMap<>();

        public void registerRoute(RouteIdentifier routeIdentifier, RouteEntry routeEntry) {
            routes.put(routeIdentifier, routeEntry);
        }

        public RouteEntry findRoute(RouteIdentifier routeIdentifier) {
            return routes.get(routeIdentifier);
        }

        public FunctionalRouter() {

            //we register the user routes
            registerRoute(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.POST.getVerb()), ExecutePostUser::new);

            registerRoute(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.GET.getVerb()), ExecuteGetUser::new);

            registerRoute(new RouteIdentifier(HttpPath.USERS.getPath(), HttpVerb.PUT.getVerb()), ExecutePutUser::new);

            //we register the session routes
            registerRoute(new RouteIdentifier(HttpPath.SESSIONS.getPath(), HttpVerb.POST.getVerb()), ExecutePostSession::new);

            //we register the packages routes
            registerRoute(new RouteIdentifier(HttpPath.PACKAGES.getPath(), HttpVerb.POST.getVerb()), ExecutePostPackage::new);

            //we register the transactions/packages route
            registerRoute(new RouteIdentifier(HttpPath.TRANSACTIONS_PACKAGES.getPath(), HttpVerb.POST.getVerb()), ExecutePostTransactionPackage::new);

            //we register the tradings route
            registerRoute(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.GET.getVerb()), ExecuteGetTrading::new);

            registerRoute(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.POST.getVerb()), ExecutePostTrading::new);

            registerRoute(new RouteIdentifier(HttpPath.TRADINGS.getPath(), HttpVerb.DELETE.getVerb()), ExecuteDeleteTrading::new);

            //we register the cards route
            registerRoute(new RouteIdentifier(HttpPath.CARDS.getPath(), HttpVerb.GET.getVerb()), ExecuteGetCard::new);

            //we register the decks route
            registerRoute(new RouteIdentifier(HttpPath.DECK.getPath(), HttpVerb.GET.getVerb()), ExecuteGetDeck::new);

            registerRoute(new RouteIdentifier(HttpPath.DECK.getPath(), HttpVerb.PUT.getVerb()), ExecutePutDeck::new);

            //we register the stats route
            registerRoute(new RouteIdentifier(HttpPath.STATS.getPath(), HttpVerb.GET.getVerb()), ExecuteGetStats::new);

            //we register the score route
            registerRoute(new RouteIdentifier(HttpPath.SCORE.getPath(), HttpVerb.GET.getVerb()), ExecuteGetScore::new);

            //we register the battles route
            registerRoute(new RouteIdentifier(HttpPath.BATTLES.getPath(), HttpVerb.PUT.getVerb()), ExecutePostBattle::new);

            registerRoute(new RouteIdentifier(HttpPath.ACTIVATE.getPath(), HttpVerb.PUT.getVerb()), ExecutePutActivate::new);

            registerRoute(new RouteIdentifier(HttpPath.DEACTIVATE.getPath(), HttpVerb.PUT.getVerb()), ExecutePutDeactivate::new);

        }

    }
