package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.AuthenticationRequest;
import edu.depaul.taskmanager.api.model.Session;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MongoAuthenticationService implements AuthenticationService {

    private UserRepository userRepository;

    @Autowired
    public MongoAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Session> login(AuthenticationRequest authenticationRequest) {
        return userRepository.findByEmailEqualsIgnoreCaseAndPasswordEquals(
                authenticationRequest.getEmail(), authenticationRequest.getPassword())
                .map(user -> Session.newBuilder()
                        .withUserId(user.getId())
                        .withFirstName(user.getFirstName())
                        .withLastName(user.getLastName())
                        .build());
    }
}
