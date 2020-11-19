package com.piorun.secure.app.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class PasswordResetToken {

    @Id
    private String id;
    private String token;
    private String email;
    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.expiryDate = LocalDateTime.now().plusDays(1);
    }
}
