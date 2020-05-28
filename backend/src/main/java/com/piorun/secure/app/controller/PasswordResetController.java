package com.piorun.secure.app.controller;


import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.verifiers.PasswordVerifier;
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

    private final UserRepository userRepository;
    private final SaltRepository saltRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordVerifier passwordVerifier;
    private final String LINK_SENT_MESSAGE = "Password reset link was sent to provided email";

    public PasswordResetController(UserRepository userRepository, SaltRepository saltRepository, PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.saltRepository = saltRepository;
        this.tokenRepository = tokenRepository;
        this.passwordVerifier = new PasswordVerifier();
    }

    @PostMapping("/reset/{token}")
    public ResponseEntity<String> resetPassword(String password, @PathVariable String token) {
        verifyToken(token);

        passwordVerifier.verify(password);
        userRepository.save(null);
        saltRepository.save(null);
        return new ResponseEntity<>(LINK_SENT_MESSAGE, HttpStatus.OK);
    }

    private void verifyToken(String token) {
        try {
            UUID uuid = UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) {
            System.out.println("thrown new Exception");
        }

        PasswordResetToken resetToken = tokenOptional.get();
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            System.out.println("Exception token too old");
        }

    }
}
