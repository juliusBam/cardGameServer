package julio.cardGame.cardGameClient.helpers;

import julio.cardGame.common.models.CardModel;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientInputValidator {
    public boolean validateUsername(String input) {

        if (input == null || input.isBlank() || input.isEmpty()) {
            System.out.print("Invalid username, input is empty, try again: ");
            return false;
        }

        if (input.contains(" ")) {
            System.out.print("Invalid username, please no whitespaces, try again: ");
            return false;
        }

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(input);

        if (m.find()) {
            System.out.print("Invalid username, please no special chars, try again: ");
            return false;
        }
        return true;
    }

    public boolean validatePwd(String input) {

        if (input == null || input.isBlank() || input.isEmpty()) {
            System.out.print("Invalid password, input is empty, try again: ");
            return false;
        }

        return true;
    }

    public UUID validateUUID(String input) {

        try {
            return UUID.fromString(input);

        } catch (IllegalArgumentException e) {
            return null;
        }

    }

    public Double validateDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean validateCardName(String input) {

        if (input.isEmpty() || input.isBlank()) {
            System.out.print("Invalid card name, cannot be empty, try again: ");
            return false;
        }

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(input);

        if (m.find()) {
            System.out.print("Invalid card name, please no special chars, try again: ");
            return false;
        }

        return true;

    }

}
