package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.verifiers.EmailVerifier;
import com.piorun.secure.app.security.verifiers.PasswordVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class PasswordResetController {

    private final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    private final UserRepository userRepository;
    private final SaltRepository saltRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordVerifier passwordVerifier;
    private final EmailVerifier emailVerifier;
    private final String MESSAGE = "Password reset link was sent to provided email";

    public PasswordResetController(UserRepository userRepository, SaltRepository saltRepository, PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.saltRepository = saltRepository;
        this.tokenRepository = tokenRepository;
        this.emailVerifier = new EmailVerifier();
        this.passwordVerifier = new PasswordVerifier();
    }

    @PostMapping("/reset")
    public String generateResetLink(String email) {
        logger.info("Received password reset link generation for email : " + email);

        if (getUserFromDbByEmail(email).isEmpty()) {
            logger.info("User with that email not found in database");
            return MESSAGE;
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, email);
        logger.info("Generated password reset token for email : " + email + ", that expires " + resetToken.getExpiryDate().toString());
        tokenRepository.deleteByEmail(email);
        tokenRepository.save(resetToken);
        return MESSAGE;
    }

    private Optional<User> getUserFromDbByEmail(String email) {
        try {
            emailVerifier.verify(email);
        } catch (VerificationException e) {
            logger.info("Provided email was not in correct format");
            return Optional.empty();
        }

        return userRepository.findByEmail(email);
    }

    @PostMapping
    public ResponseEntity<String> resetPassword() {
        return null;
    }
}
