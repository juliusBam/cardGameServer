package julio.cardGame.cardGameServer.http.routing;

public enum HttpVerb {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),

    DELETE("DELETE");

    private final String httpVerb;

    HttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }

    public String getVerb() {
        return this.httpVerb;
    }

}
