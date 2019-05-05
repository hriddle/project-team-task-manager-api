package edu.depaul.taskmanager.api.controller;

import edu.depaul.taskmanager.api.model.User;
import edu.depaul.taskmanager.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Deprecated
@RequestMapping("/operations")
public class OperationsController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity helloWorld() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/users")
    public ResponseEntity helloWorld(@RequestBody User demoUser, UriComponentsBuilder uriComponentsBuilder) {
        User savedDemoUser = userRepository.save(demoUser);
        return ResponseEntity.created(uriComponentsBuilder.path("/users/{id}")
                .buildAndExpand(savedDemoUser.getId())
                .toUri())
                .build();
    }
}
