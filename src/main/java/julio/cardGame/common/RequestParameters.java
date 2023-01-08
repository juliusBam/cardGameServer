package julio.cardGame.common;

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
