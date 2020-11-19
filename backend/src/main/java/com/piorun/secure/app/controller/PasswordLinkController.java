package com.piorun.secure.app.controller;

import com.piorun.secure.app.mail.EmailSender;
import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import com.piorun.secure.app.service.PasswordTokenService;
import com.piorun.secure.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PasswordLinkController {

    private final UserService userService;
    private final PasswordTokenService tokenService;
    private final EmailSender emailService;

    @PostMapping("/reset")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> generateResetLink(String email) {
        log.info("Received password reset link generation for email : " + email);

        final String LINK_SENT_MESSAGE = "Password reset link was sent to provided email";

        if (userService.findByEmail(email).isEmpty()) {
            log.info("User with that email not found in database");
            return new ResponseEntity<>(LINK_SENT_MESSAGE, HttpStatus.OK);
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, email);
        log.info("Generated password reset token for email : " + email + ", that expires " + resetToken.getExpiryDate().toString());
        tokenService.deleteByEmail(email);
        tokenService.save(resetToken);

        emailService.sendMail(email, "Password reset link", "localhost:8080/reset/" + token);
        return new ResponseEntity<>(LINK_SENT_MESSAGE, HttpStatus.OK);
    }
}
