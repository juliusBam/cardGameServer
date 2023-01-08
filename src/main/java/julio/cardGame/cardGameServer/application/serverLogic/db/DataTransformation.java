package julio.cardGame.cardGameServer.application.serverLogic.db;

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
}
