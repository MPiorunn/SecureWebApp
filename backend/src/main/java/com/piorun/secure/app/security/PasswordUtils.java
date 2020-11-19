package com.piorun.secure.app.security;

import com.piorun.secure.app.exception.PasswordResetException;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordUtils {

    private static String PEPPER = "someDefaultValue";

    @Value("${spring.security.pepper}")
    public void setDatabase(String pepper) {
        PEPPER = pepper;
    }

    public static String generateSalt() {
        return BCrypt.gensalt();
    }

    public static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(PEPPER + password, salt).substring(salt.length());
    }

    public static boolean checkHash(String hash, String password, String salt) {
        return hash.equals(hashPassword(password, salt));
    }
}
