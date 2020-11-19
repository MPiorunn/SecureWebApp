package com.piorun.secure.app.service;

import com.piorun.secure.app.exception.PasswordResetException;
import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class PasswordTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken getTokenFromDatabase(String token) {
        try {
            UUID.fromString(token);
        } catch (IllegalArgumentException e) {
            log.info("Provided token " + token + "was in incorrect for UUID format");
            throw new PasswordResetException("Incorrect token format");
        }
        log.info("Provided token was in correct for UUID format");

        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) {
            log.info("Token " + token + " was not found in the database");
            throw new PasswordResetException("Token not found in the database");
        }
        log.info("Token " + token + " was found in the database");

        PasswordResetToken resetToken = tokenOptional.get();
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.info("Token " + token + " has expired on " + resetToken.getExpiryDate().toString());
            throw new PasswordResetException("Token already expired");
        }

        log.info("Token " + token + " is valid");
        return resetToken;
    }

    private Optional<PasswordResetToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deleteByEmail(String email) {
        tokenRepository.deleteByEmail(email);
    }

    public PasswordResetToken save(PasswordResetToken resetToken) {
        return tokenRepository.save(resetToken);
    }
}
