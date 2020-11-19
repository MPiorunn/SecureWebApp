package com.piorun.secure.app.controller;

import com.piorun.secure.app.exception.PasswordResetException;
import com.piorun.secure.app.model.PasswordResetToken;
import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.model.User;
import com.piorun.secure.app.security.verifiers.PasswordVerifier;
import com.piorun.secure.app.service.PasswordTokenService;
import com.piorun.secure.app.service.SaltService;
import com.piorun.secure.app.service.UserService;
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
    private final UserService userService = mock(UserService.class);
    private final SaltService saltService = mock(SaltService.class);
    private final PasswordTokenService tokenService = mock(PasswordTokenService.class);

    @Test
    public void shouldReturn400WhenPasswordWrong() {
        String password = "wrong";
        String path = RESET_PATH + "/" + correctToken;
        given()
                .standaloneSetup(new PasswordResetController(userService, saltService, tokenService, new PasswordVerifier()))
                .param(PASSWORD, password)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenService, never()).getTokenFromDatabase(anyString());
    }

    @Test
    public void shouldReturn400WhenTokenNotUUID() {
        String token = "Definitely not UUID";
        String path = RESET_PATH + "/" + token;
        when(tokenService.getTokenFromDatabase(token)).thenCallRealMethod();
        given()
                .standaloneSetup(new PasswordResetController(userService, saltService, tokenService, new PasswordVerifier()))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenService).getTokenFromDatabase(token);
    }

    @Test
    public void shouldReturn400WhenTokenNotFound() {
        String path = RESET_PATH + "/" + correctToken;
        when(tokenService.getTokenFromDatabase(correctToken)).thenThrow(PasswordResetException.class);
        given()
                .standaloneSetup(new PasswordResetController(userService, saltService, tokenService, new PasswordVerifier()))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenService, times(1)).getTokenFromDatabase(correctToken);
    }

    @Test
    public void shouldReturn400WhenTokenExpired() {
        PasswordResetToken resetToken = mock(PasswordResetToken.class);
        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().minusDays(2));
        String path = RESET_PATH + "/" + correctToken;
        when(tokenService.getTokenFromDatabase(correctToken)).thenReturn(resetToken);
        given()
                .standaloneSetup(new PasswordResetController(userService, saltService, tokenService, new PasswordVerifier()))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenService, times(1)).getTokenFromDatabase(correctToken);
    }

    @Test
    public void shouldReturn400WhenUserNotFound() {
        PasswordResetToken resetToken = mock(PasswordResetToken.class);
        String path = RESET_PATH + "/" + correctToken;
        String email = "example.mail@website.com";

        when(resetToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusHours(3));
        when(resetToken.getEmail()).thenReturn(email);
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(tokenService.getTokenFromDatabase(correctToken)).thenReturn(resetToken);
        given()
                .standaloneSetup(new PasswordResetController(userService, saltService, tokenService, new PasswordVerifier()))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenService, times(1)).getTokenFromDatabase(correctToken);
        verify(userService, times(1)).findByEmail(email);
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
        when(saltService.findById(saltId)).thenReturn(Optional.of(new Salt(salt)));

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenService.getTokenFromDatabase(correctToken)).thenReturn(resetToken);
        given()
                .standaloneSetup(new PasswordResetController(userService, saltService, tokenService, new PasswordVerifier()))
                .param(PASSWORD, correctPassword)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);

        verify(tokenService, times(1)).getTokenFromDatabase(correctToken);
        verify(userService, times(1)).findByEmail(email);
    }
}