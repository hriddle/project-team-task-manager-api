package edu.depaul.taskmanager.api.service;

import edu.depaul.taskmanager.api.model.AuthenticationRequest;
import edu.depaul.taskmanager.api.model.Session;

import java.util.Optional;

public interface AuthenticationService {
    Optional<Session> login(AuthenticationRequest authenticationRequest);
}
