package com.piorun.secure.app.repository;

import com.piorun.secure.app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
