package julio.cardGame.cardGameClient.helpers;

public enum ClientAction {
    BATTLE("fight"),
    LOGIN("login"),
    SHOW_CARDS("show cards"),
    TRADE("trade"),
    BUY_PACKAGE("buy"),
    CREATE_USER("add user"),

    CREATE_PACKAGES("create packs"),

    SHOW_DECK("show deck"),

    CREATE_DECK("create deck"),

    UPDATE_USER("update user data"),

    SHOW_USER("show user data"),

    SHOW_STATS("show stats"),

    SHOW_SCORE("show score"),

    SHOW_TRADES("show trades"),

    POST_TRADE("add trade"),

    DELETE_TRADE_DEAL("delete trade"),

    QUIT("q");

    private final String clientAction;

    ClientAction(String clientAction) {
        this.clientAction = clientAction;
    }

    public String getClientAction() {
        return this.clientAction;
    }
}
