package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.AuthenticationRequest;
import edu.depaul.taskmanager.api.model.RegistrationRequest;
import edu.depaul.taskmanager.api.model.Session;
import edu.depaul.taskmanager.api.service.AuthenticationService;
import edu.depaul.taskmanager.api.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
public class UserController {

    private AuthenticationService authenticationService;
    private RegistrationService registrationService;

    @Autowired
    public UserController(AuthenticationService authenticationService, RegistrationService registrationService) {
        this.authenticationService = authenticationService;
        this.registrationService = registrationService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/sessions")
    public ResponseEntity login(@RequestBody AuthenticationRequest authenticationRequest) {
        Optional<Session> optionalSession = authenticationService.login(authenticationRequest);
        return optionalSession.map(user -> ResponseEntity.ok(user))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/users")
    public ResponseEntity<Session> register(@RequestBody RegistrationRequest registrationRequest, UriComponentsBuilder uriComponentsBuilder) {

        return registrationService.register(registrationRequest)
                .map(user -> ResponseEntity
                        .created(uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri())
                        .body(Session.newBuilder()
                                .withFirstName(user.getFirstName())
                                .withLastName(user.getLastName())
                                .withUserId(user.getId())
                                .build())
                ).orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
