package julio.cardGame.cardGameServer.application.serverLogic.db;

import julio.cardGame.common.CardTypes;
import org.postgresql.util.PGobject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;

public class DataTransformation {
    public static String calculateHash(String input) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] messageDigest = md.digest(input.getBytes());

        BigInteger no = new BigInteger(1,messageDigest);

        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
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
            return (double) (wins / (wins + losses));

        return 0;

    }
}
