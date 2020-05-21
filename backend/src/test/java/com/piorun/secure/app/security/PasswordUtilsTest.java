package com.piorun.secure.app.security;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordUtilsTest {

    @Test
    public void shouldGenerateDifferentSalts() {
        String salt1 = PasswordUtils.generateSalt();
        String salt2 = PasswordUtils.generateSalt();
        assertNotEquals(salt1, salt2);
    }

    @Test
    public void hashingAlgorithmTest(){
        String salt = PasswordUtils.generateSalt();
        String password = "MyPAssw0rd!";
        String hash = PasswordUtils.hashPassword(password, salt);
        boolean b = PasswordUtils.chechHash(hash, password, salt);
        assertTrue(b);
    }

}