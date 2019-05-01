package edu.depaul.taskmanager.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Deprecated
public class HelloWorldController {

    @GetMapping("/hello")
    public ResponseEntity helloWorld(){
        return new ResponseEntity("hello world", HttpStatus.I_AM_A_TEAPOT);
    }
}
