package com.piorun.secure.app.mail;

import com.piorun.secure.app.exception.VerificationException;
import com.piorun.secure.app.security.verifiers.EmailFormatVerifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    private final JavaMailSender emailSender;
    private final EmailFormatVerifier emailFormatVerifier = new EmailFormatVerifier();

    public EmailSender(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMail(String to, String subject, String text) {
        verifyInputParameters(to, subject, text);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);
        emailSender.send(mail);
    }

    private void verifyInputParameters(String to, String subject, String text) {
        try {
            emailFormatVerifier.verify(to);
        } catch (VerificationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Cannot send email without providing the subject");
        }
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Cannot send email without providing the content");
        }
    }

}

