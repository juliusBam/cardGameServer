package julio.cardGame.cardGameServer.database.db;

import julio.cardGame.cardGameServer.battle.cards.CardTypes;
import julio.cardGame.cardGameServer.Constants;
import org.postgresql.util.PGobject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;

public class DataTransformation {

    public static final int winValue = 1;

    public static final int loseValue = -1;
    public static String calculateHash(String input) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] messageDigest = md.digest(input.getBytes());

        BigInteger no = new BigInteger(1,messageDigest);

        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = hashtext.concat("0");
        }
        return hashtext;

    }

    public static PGobject prepareUUID(UUID uuid) throws SQLException {

        PGobject transformedUUID = new PGobject();

        transformedUUID.setType("uuid");
        transformedUUID.setValue(uuid.toString());

        return transformedUUID;
    }

    public static CardTypes convertIntoCardType(String rawCardType) {

        if (rawCardType.equals("monster"))
            return CardTypes.MONSTER;

        if (rawCardType.equals("spell"))
            return CardTypes.SPELL;

        return null;

    }

    public static double calculateWinRate(int wins, int losses) {

        if (wins + losses != 0 || wins != 0)
            return (wins / (wins + losses));

        return 0;

    }

    public static String createAuthToken(String userName) {

        return String.format(Constants.USER_AUTH, userName);

    }

    public static int calculateWinnerElo(int winnerElo, int loserElo) {

        int eloIncrease = 32;

        if (winnerElo > 1600)
            eloIncrease = 16;

        int expectedOutcome = winnerElo > loserElo ? DataTransformation.winValue : -1;

        int newElo = winnerElo + (eloIncrease * (DataTransformation.winValue - expectedOutcome));

        return Math.max(newElo, 0);

    }

    public static int calculateLoserElo(int winnerElo, int loserElo) {

        int eloIncrease = 32;

        if (loserElo > 1600)
            eloIncrease = 16;

        int expectedOutcome = loserElo > winnerElo ? DataTransformation.winValue : -1;

        int newElo = winnerElo + (eloIncrease * (DataTransformation.winValue - expectedOutcome));

        return Math.max(newElo, 0);
    }
}
