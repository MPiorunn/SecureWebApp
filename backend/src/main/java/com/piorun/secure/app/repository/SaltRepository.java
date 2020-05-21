package com.piorun.secure.app.repository;

import com.piorun.secure.app.model.Salt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaltRepository extends MongoRepository<Salt, String> {
}
