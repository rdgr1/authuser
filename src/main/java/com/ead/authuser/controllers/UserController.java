package com.ead.authuser.controllers;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    final UserServiceImpl service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(userId).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        service.delete(service.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }
}
