package com.piorun.secure.app.mail;

import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    private final JavaMailSender javaMailSender = mock(JavaMailSender.class);
    private final EmailSender emailSender = new EmailSender(javaMailSender);

    @Test
    public void shouldSendEmailWhenInputCorrect() {
        String to = "correct@email.com";
        String subject = "Some subject";
        String content = "EMail content";
        emailSender.sendMail(to, subject, content);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenEmailNull() {
        String to = null;
        String subject = "Some subject";
        String content = "EMail content";
        emailSender.sendMail(to, subject, content);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenEmailIncorrect() {
        String to = "incorrectemail";
        String subject = "Some subject";
        String content = "EMail content";
        emailSender.sendMail(to, subject, content);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenEmailEmpty() {
        String to = "";
        String subject = "Some subject";
        String content = "EMail content";
        emailSender.sendMail(to, subject, content);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenSubjectNull() {
        String to = "correct@email.com";
        String subject = "Some subject";
        String content = null;
        emailSender.sendMail(to, subject, content);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenSubjectEmpty() {
        String to = "correct@email.com";
        String subject = "";
        String content = "Content";
        emailSender.sendMail(to, subject, content);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenContentNull() {
        String to = "correct@email.com";
        String subject = "some subject";
        String content = null;
        emailSender.sendMail(to, subject, content);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenContentEmpty() {
        String to = "correct@email.com";
        String subject = "some subject";
        String content = "";
        emailSender.sendMail(to, subject, content);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }


}