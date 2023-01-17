package julio.cardGame.cardGameServer.http.routing.router;

import julio.cardGame.cardGameServer.http.routing.routes.Routeable;

public interface RouteEntry {
    public Routeable generateRoute();
}
