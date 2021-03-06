package com.piorun.secure.app.repository;

import com.piorun.secure.app.model.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {

    void deleteByEmail(String email);

    Optional<PasswordResetToken> findByToken(String token);
}
