package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.mail.EmailSender;
import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.verifiers.EmailFormatVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class PasswordLinkController {

    private final Logger logger = LoggerFactory.getLogger(PasswordLinkController.class);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailFormatVerifier emailFormatVerifier;
    private final EmailSender emailService;

    public PasswordLinkController(UserRepository userRepository, PasswordResetTokenRepository tokenRepository, EmailSender emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.emailFormatVerifier = new EmailFormatVerifier();
    }

    @PostMapping("/reset")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> generateResetLink(String email) {
        logger.info("Received password reset link generation for email : " + email);

        final String LINK_SENT_MESSAGE = "Password reset link was sent to provided email";

        if (getUserFromDbByEmail(email).isEmpty()) {
            logger.info("User with that email not found in database");
            return new ResponseEntity<>(LINK_SENT_MESSAGE, HttpStatus.OK);
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, email);
        logger.info("Generated password reset token for email : " + email + ", that expires " + resetToken.getExpiryDate().toString());
        tokenRepository.deleteByEmail(email);
        tokenRepository.save(resetToken);

        emailService.sendMail(email, "Password reset link", "localhost:8080/reset/" + token);
        return new ResponseEntity<>(LINK_SENT_MESSAGE, HttpStatus.OK);
    }


    private Optional<User> getUserFromDbByEmail(String email) {
        try {
            emailFormatVerifier.verify(email);
        } catch (VerificationException e) {
            logger.info("Provided email was not in correct format");
            return Optional.empty();
        }

        return userRepository.findByEmail(email);
    }
}
