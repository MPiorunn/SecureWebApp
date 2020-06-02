package com.piorun.secure.app.controller;


import com.piorun.secure.app.exception.PasswordResetException;
import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.PasswordUtils;
import com.piorun.secure.app.security.verifiers.PasswordVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PasswordResetController {

    private final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    private final UserRepository userRepository;
    private final SaltRepository saltRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordVerifier passwordVerifier;
    private final String LINK_SENT_MESSAGE = "Password reset successful";

    public PasswordResetController(UserRepository userRepository, SaltRepository saltRepository, PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.saltRepository = saltRepository;
        this.tokenRepository = tokenRepository;
        this.passwordVerifier = new PasswordVerifier();
    }

    @PostMapping("/reset/{token}")
    public ResponseEntity<String> resetPassword(String password, @PathVariable String token) {
        logger.info("Received password reset request for token " + token);
        try {
            passwordVerifier.verify(password);
        } catch (VerificationException e) {
            throw new PasswordResetException("Pick the better password!");
        }

        PasswordResetToken resetToken = getTokenFromDatabase(token);

        Optional<User> userOptional = userRepository.findByEmail(resetToken.getEmail());

        checkIfUserGeneratedThatToken(userOptional);

        User user = userOptional.get();

        verifyIfPasswordsAreDifferent(user, password);

        generateNewPasswordForUser(user, password);

        return new ResponseEntity<>(LINK_SENT_MESSAGE, HttpStatus.OK);
    }

    private void checkIfUserGeneratedThatToken(Optional<User> userOptional) {
        if (userOptional.isEmpty()) {
            throw new PasswordResetException("No user has generated this reset token");
        }
    }

    private void verifyIfPasswordsAreDifferent(User user, String password) {
        String saltId = user.getSaltId();
        Salt userSalt = saltRepository.findById(saltId).get();
        boolean hashesAreEqual = PasswordUtils.checkHash(user.getHash(), password, userSalt.getValue());
        if (hashesAreEqual) {
            logger.info("User provided the same password as before!");
            throw new PasswordResetException("Pick the better password!");
        }
    }

    private void generateNewPasswordForUser(User user, String password) {
        String username = user.getUsername();

        logger.info("Generating new hash for user " + username);

        String newSalt = PasswordUtils.generateSalt();
        String newHash = PasswordUtils.hashPassword(password, newSalt);
        String saltId = user.getSaltId();

        logger.info("Removing old Salt with ID " + saltId + " for user " + username);
        Salt savedSalt = saltRepository.save(new Salt(newSalt));

        logger.info("Hash and new Salt Id changed for user " + username);
        user.setHash(newHash);
        user.setSaltId(savedSalt.getId());

        // remove old data
        saltRepository.deleteById(saltId);
        userRepository.deleteById(user.getId());
        tokenRepository.deleteByEmail(user.getEmail());

        userRepository.save(user);

        logger.info("Password reset successful");
    }

    private PasswordResetToken getTokenFromDatabase(String token) {
        try {
            UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            logger.info("Provided token " + token + "was in incorrect for UUID format");
            throw new PasswordResetException("Incorrect token format");
        }
        logger.info("Provided token was in correct for UUID format");

        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) {
            logger.info("Token " + token + " was not found in the database");
            throw new PasswordResetException("Token not found in the database");
        }
        logger.info("Token " + token + " was found in the database");

        PasswordResetToken resetToken = tokenOptional.get();
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.info("Token " + token + " has expired on " + resetToken.getExpiryDate().toString());
            throw new PasswordResetException("Token already expired");
        }

        return resetToken;
    }
}
