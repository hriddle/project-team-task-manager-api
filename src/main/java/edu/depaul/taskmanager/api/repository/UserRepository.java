package edu.depaul.taskmanager.api.repository;

import edu.depaul.taskmanager.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailEqualsIgnoreCaseAndPasswordEquals(String email, String password);
}
