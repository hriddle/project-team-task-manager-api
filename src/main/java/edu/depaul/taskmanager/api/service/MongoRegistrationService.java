package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.RegistrationRequest;
import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MongoRegistrationService implements RegistrationService {

    private UserRepository userRepository;

    public MongoRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> register(RegistrationRequest registrationRequest) {
        if (!registrationRequest.getPassword().equals(registrationRequest.getPasswordConfirmation())) {
            return Optional.empty();
        }
        User createdUser = userRepository.save(User.newBuilder()
                .withFirstName(registrationRequest.getFirstName())
                .withLastName(registrationRequest.getLastName())
                .withEmail(registrationRequest.getEmail())
                .withPassword(registrationRequest.getPassword())
                .build());
        return Optional.of(createdUser);
    }
}
