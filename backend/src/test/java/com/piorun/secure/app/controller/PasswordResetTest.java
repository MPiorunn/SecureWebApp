package com.piorun.secure.app.controller;

import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import com.piorun.secure.app.security.PasswordUtils;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.mockito.Mockito.*;


public class PasswordResetTest {

    private final String RESET_PATH = "/reset";
    private final String PASSWORD = "password";
    private final String correctToken = UUID.randomUUID().toString();
    private final String correctPassword = "PAssw0rd!!@";
    private final UserRepository userRepository = mock(UserRepository.class);
    private final SaltRepository saltRepository = mock(SaltRepository.class);
    private final PasswordResetTokenRepository tokenRepository = mock(PasswordResetTokenRepository.class);

    @Test
    public void shouldReturn400WhenPasswordWrong() {
        String password = "wrong";
        String path = RESET_PATH + "/" + correctToken;
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(PASSWORD, password)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenRepository, never()).findByToken(anyString());
    }

    @Test
    public void shouldReturn400WhenTokenNotUUID() {
        String token = "Definitely not UUID";
        String path = RESET_PATH + "/" + token;
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenRepository, never()).findByToken(token);
    }

    @Test
    public void shouldReturn400WhenTokenNotFound() {
        String path = RESET_PATH + "/" + correctToken;
        when(tokenRepository.findByToken(correctToken)).thenReturn(Optional.empty());
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenRepository, times(1)).findByToken(correctToken);
    }

    @Test
    public void shouldReturn400WhenTokenExpired() {
        PasswordResetToken resetToken = mock(PasswordResetToken.class);
        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().minusDays(2));
        String path = RESET_PATH + "/" + correctToken;
        when(tokenRepository.findByToken(correctToken)).thenReturn(Optional.of(resetToken));
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenRepository, times(1)).findByToken(correctToken);
        verify(resetToken, times(2)).getExpiryDate();
    }

    @Test
    public void shouldReturn400WhenUserNotFound() {
        PasswordResetToken resetToken = mock(PasswordResetToken.class);
        String path = RESET_PATH + "/" + correctToken;
        String email = "example.mail@website.com";

        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusHours(3));
        when(resetToken.getEmail()).thenReturn(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(tokenRepository.findByToken(correctToken)).thenReturn(Optional.of(resetToken));
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenRepository, times(1)).findByToken(correctToken);
        verify(resetToken, times(1)).getExpiryDate();
        verify(userRepository, times(1)).findByEmail(email);
    }


    @Test
    public void shouldReturn400WhenPasswordIsTheSame() {
        PasswordResetToken resetToken = mock(PasswordResetToken.class);
        User user = mock(User.class);
        String path = RESET_PATH + "/" + correctToken;
        String email = "example.mail@website.com";
        String saltId = "some Id";
        String salt = "$2a$10$2y.gkO39MVR8yLKZlgevmO";

        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusHours(3));
        when(resetToken.getEmail()).thenReturn(email);

        when(user.getSaltId()).thenReturn(saltId);
        when(user.getHash()).thenReturn("QPgpyoJfypYvuGVYunDgtFJOz5nbC72");
        when(saltRepository.findById(saltId)).thenReturn(Optional.of(new Salt(salt)));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenRepository.findByToken(correctToken)).thenReturn(Optional.of(resetToken));
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenRepository, times(1)).findByToken(correctToken);
        verify(resetToken, times(1)).getExpiryDate();
        verify(userRepository, times(1)).findByEmail(email);
    }
}