package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final UserService service;
    Logger logger = LogManager.getLogger(AuthenticationController.class);
    public AuthenticationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserRecordDto.UserView.RegistrationPost.class) @JsonView(UserRecordDto.UserView.RegistrationPost.class) UserRecordDto dto){
        logger.debug("POST registerUser received {}",dto);
        if (service.existsByUsername(dto.username())){
            logger.warn("Username {} is Already Taken!", dto.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }
        if (service.existsByEmail(dto.email())){
            logger.warn("Email {} is Already Taken!", dto.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(dto));
    }
}
