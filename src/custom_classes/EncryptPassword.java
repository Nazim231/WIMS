package custom_classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptPassword {


    public static String encrypt(String text) {
        
        String encryptedText = null;

        if (text.length() == 0)
            return encryptedText;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(text.getBytes());
            BigInteger bno = new BigInteger(1, bytes);
            encryptedText = bno.toString(16);
            while (encryptedText.length() < 32) {
                encryptedText = "0" + encryptedText;
            }

            return encryptedText;

        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
            return null;
        }
        
    }
}
