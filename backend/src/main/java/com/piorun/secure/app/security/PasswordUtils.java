package com.piorun.secure.app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {

    private static String PEPPER;

    @Value("${spring.security.pepper}")
    public void setDatabase(String pepper) {
        PEPPER = pepper;
    }

    public static String generateSalt() {
        return BCrypt.gensalt();
    }

    public static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(PEPPER + password, salt);
    }

    public static boolean checkPassword(String hash, String password, String salt) {
        return hash.equals(hashPassword(PEPPER + password, salt));
    }
}