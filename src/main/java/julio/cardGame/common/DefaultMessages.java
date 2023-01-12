package julio.cardGame.common;

public enum DefaultMessages {

    ERR_JSON_PARSE_USER("Error while parsing the user data"),

    ERR_NO_COINS("Not enough coins, to buy the package"),

    ERR_BATTLE_RES_NULL("An error occurred in the battle logic"),

    ERR_USERS_BATTLE_QUEUE("Could not retrieve the users from the battle queue"),

    ERR_NO_PACKAGES("There are no packages to buy"),

    ERR_CARD_NOT_OWNED("The offered card is not owned by you"),

    ERR_CARD_NOT_VALID("The offered card is not valid for the trade"),

    ERR_NO_SELF_TRADE("No self trades allowed"),

    ERR_JSON_PARSE_STATS("Error while parsing the stats"),

    ERR_MISMATCHING_USERS("The requested and logged user do not match"),

    USER_NO_CARDS("No cards were found"),

    USER_NO_DECK("No deck was found"),

    SCORE_NO_RESULTS("No scoreboard found"),

    ERR_NO_FOUND_USER("The requested user could not be found"),

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
