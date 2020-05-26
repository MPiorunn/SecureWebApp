package com.piorun.secure.app.controller;

import com.piorun.secure.app.model.User;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PasswordResetLinkGenerationTest {

    private final String RESET_PATH = "/reset";
    private final String EMAIL = "email";
    private final UserRepository userRepository = mock(UserRepository.class);
    private final SaltRepository saltRepository = mock(SaltRepository.class);
    private final PasswordResetTokenRepository tokenRepository = mock(PasswordResetTokenRepository.class);

    @Test
    public void shouldReturn200WhenInputOk() {
        String email = "jeff.fafa@dot.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(EMAIL, email)
                .when()
                .post(RESET_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.OK);

        verify(tokenRepository, times(1)).deleteByEmail(email);
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    public void shouldReturn200WhenWrongEmailFormat() {
        String email = "jeff.fafa";
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(EMAIL, email)
                .when()
                .post(RESET_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.OK);

        verify(tokenRepository, times(0)).deleteByEmail(email);
        verify(tokenRepository, times(0)).save(any());
    }

    @Test
    public void shouldReturn200WhenUserNotFoundByEmail() {
        String email = "jeff.fafa@dot.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(EMAIL, email)
                .when()
                .post(RESET_PATH)
                .then()
                .assertThat()
                .status(HttpStatus.OK);

        verify(tokenRepository, times(0)).deleteByEmail(email);
        verify(tokenRepository, times(0)).save(any());
    }
}