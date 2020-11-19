package com.piorun.secure.app.controller;


import com.piorun.secure.app.exception.PasswordResetException;
import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.security.PasswordUtils;
import com.piorun.secure.app.security.verifiers.PasswordVerifier;
import com.piorun.secure.app.service.PasswordTokenService;
import com.piorun.secure.app.service.SaltService;
import com.piorun.secure.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final UserService userService;
    private final SaltService saltService;
    private final PasswordTokenService tokenService;
    private final PasswordVerifier passwordVerifier;

    @PostMapping("/reset/{token}")
    public ResponseEntity<String> resetPassword(String password, @PathVariable String token) {
        log.info("Received password reset request for token " + token);
        try {
            passwordVerifier.verify(password);
        } catch (VerificationException e) {
            throw new PasswordResetException("Pick the better password!");
        }

        PasswordResetToken resetToken = tokenService.getTokenFromDatabase(token);

        Optional<User> userOptional = userService.findByEmail(resetToken.getEmail());

        checkIfUserGeneratedThatToken(userOptional);

        User user = userOptional.get();

        verifyIfPasswordsAreDifferent(user, password);

        generateNewPasswordForUser(user, password);

        return new ResponseEntity<>("Password reset successful", HttpStatus.OK);
    }

    // move this to other classes
    private void checkIfUserGeneratedThatToken(Optional<User> userOptional) {
        if (userOptional.isEmpty()) {
            throw new PasswordResetException("No user has generated this reset token");
        }
    }

    private void verifyIfPasswordsAreDifferent(User user, String password) {
        String saltId = user.getSaltId();
        Salt userSalt = saltService.findById(saltId).get();
        boolean hashesAreEqual = PasswordUtils.checkHash(user.getHash(), password, userSalt.getValue());
        if (hashesAreEqual) {
            log.info("User provided the same password as before!");
            throw new PasswordResetException("Pick the better password!");
        }
    }

    private void generateNewPasswordForUser(User user, String password) {
        String username = user.getUsername();

        log.info("Generating new hash for user " + username);

        String newSalt = PasswordUtils.generateSalt();
        String newHash = PasswordUtils.hashPassword(password, newSalt);
        String saltId = user.getSaltId();

        log.info("Removing old Salt with ID " + saltId + " for user " + username);
        Salt savedSalt = saltService.save(new Salt(newSalt));

        log.info("Hash and new Salt Id changed for user " + username);
        user.setHash(newHash);
        user.setSaltId(savedSalt.getId());

        // remove old data
        saltService.deleteById(saltId);
        userService.deleteById(user.getId());
        tokenService.deleteByEmail(user.getEmail());

        userService.save(user);

        log.info("Password reset successful");
    }
}
