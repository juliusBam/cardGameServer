package julio.cardGame.common;

public enum DefaultMessages {

    ERR_JSON_PARSE_USER("Error while parsing the user data"),

    ERR_JSON_PARSE_STATS("Error while parsing the stats"),

    USER_NO_CARDS("No cards were found"),

    USER_NO_DECK("No deck was found"),

    SCORE_NO_RESULTS("No scoreboard found"),

    NO_TRADES("No trades could be found"),

    ERR_INVALID_TRADE_UUID("Invalid trade uuid"),

    ERR_JSON_PARSE_SCOREBOARD("Error while parsing the scoreboard"),

    ERR_JSON_PARSE_TRADE("Error while parsing the trade details"),

    ERR_NO_USER("No user was specified"),

    ERR_JSON_PARSE_CARDS("Error while parsing the user's cards"),

    ERR_JSON_PARSE_DECK("Error while parsing the user's deck"),

    ERR_JSON_PARSE_PACKAGE("Error while parsing the packages");


    private final String message;

    DefaultMessages(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
