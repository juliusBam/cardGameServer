package julio.cardGame.cardGameServer.router;

public class RouteIdentifier {
    private final String httpPath;
    private final String httpVerb;

    public RouteIdentifier(String path, String httpVerb) {
        this.httpPath = path;
        this.httpVerb = httpVerb;
    }

    //needs to override it, so that we can use the route identifier to compare the objects in the hash map
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }

        RouteIdentifier routeIdentifier = (RouteIdentifier) otherObject;

        return this.httpPath.equals(routeIdentifier.httpPath)
                && this.httpVerb.equals(routeIdentifier.httpVerb);
    }

    @Override
    public int hashCode() {
        int pathRes = this.httpPath.hashCode();
        int verbRes = this.httpVerb.hashCode();
        return (31 * pathRes + verbRes);
    }

}
