package julio.cardGame.cardGameServer.http.communication;

//defines the different possible request parameters we use

public enum RequestParameters {
    USERNAME("userName"),

    SELECTED_TRADE_DEAL("tradeDeal"),

    FORMAT("format");

    private final String paramValue;

    RequestParameters(String param) {
        this.paramValue = param;
    }

    public String getParamValue() {
        return this.paramValue;
    }
}
