package com.piorun.secure.app.controller;

import com.piorun.secure.app.mail.EmailSender;
import com.piorun.secure.app.repository.PasswordResetTokenRepository;
import com.piorun.secure.app.repository.SaltRepository;
import com.piorun.secure.app.repository.UserRepository;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.module.webtestclient.RestAssuredWebTestClient.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PasswordResetTest {

    private final String RESET_PATH = "/reset";
    private final String PASSWORD = "password";
    private final UserRepository userRepository = mock(UserRepository.class);
    private final SaltRepository saltRepository = mock(SaltRepository.class);
    private final PasswordResetTokenRepository tokenRepository = mock(PasswordResetTokenRepository.class);

//    @Test
    public void shouldReturn200WhenInputOk() {
        String uuid = UUID.randomUUID().toString();
        String password = "xD";
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
        String path = RESET_PATH + "/" + uuid;
        given()
                .standaloneSetup(new PasswordResetController(userRepository, saltRepository, tokenRepository))
                .param(PASSWORD, password)
                .when()
                .post(path)
                .then()
                .assertThat()
                .status(HttpStatus.OK);

//        verify(tokenRepository, times(1)).deleteByEmail(email);
//        verify(tokenRepository, times(1)).save(any());
    }

}