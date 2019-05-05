package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.RegistrationRequest;
import edu.depaul.taskmanager.api.model.User;

import java.util.Optional;

public interface RegistrationService {
    Optional<User> register(RegistrationRequest registrationRequest);
}
