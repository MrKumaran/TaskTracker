package in.project.tasktracker.Core;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

public class Authentication {
    private final static int SALT_LENGTH = 10;

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateSalt() {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        while (sb.length() < SALT_LENGTH) {
            sb.append((char) (rand.nextInt(26) + 'a'));
        }
        return sb.toString();
    }

    public static String passwordHash(String password, String salt) {
        String hashable = password+salt;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        md.update(hashable.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
