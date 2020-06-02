package com.piorun.secure.app.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSenderProvider {

    @Value("spring.mail.username")
    private String username;

    @Value("spring.mail.password")
    private String password;

    @Value("spring.mail.host")
    private String host;

    @Value("spring.mail.port")
    private int port;

    @Bean
    public JavaMailSender create() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        return mailSender;
    }
}
