package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final UserService service;

    public AuthenticationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid UserRecordDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(dto));
    }
}
