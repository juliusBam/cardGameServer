package julio.cardGame.cardGameServer.http.routing;

public enum HttpPath {
    USERS("/users"),
    SESSIONS("/sessions"),
    PACKAGES("/packages"),
    TRANSACTIONS_PACKAGES("/transactions/packages"),
    CARDS("/cards"),
    DECK("/deck"),
    STATS("/stats"),
    SCORE("/score"),
    BATTLES("/battles"),
    ACTIVATE("/activate"),
    DEACTIVATE("/deactivate"),
    TRADINGS("/tradings");

    private final String HttpPath;
    HttpPath(String HttpPath) {
        this.HttpPath = HttpPath;
    }

    public String getPath() {
        return this.HttpPath;
    }
}
