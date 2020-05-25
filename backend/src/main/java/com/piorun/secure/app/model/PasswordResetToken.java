package com.piorun.secure.app.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class PasswordResetToken {

    @Id
    private String id;
    private String token;
    private String email;
    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, String username) {
        this.token = token;
        this.email = username;
        this.expiryDate = LocalDateTime.now().plusDays(1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
