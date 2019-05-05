package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.AuthenticationRequest;
import edu.depaul.taskmanager.api.model.Session;
import edu.depaul.taskmanager.api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private AuthenticationService authenticationService;

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sessions")
    public ResponseEntity login(@RequestBody AuthenticationRequest authenticationRequest) {
        Optional<Session> optionalSession = authenticationService.login(authenticationRequest);
        return optionalSession.map(user -> ResponseEntity.ok(user))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }
}
