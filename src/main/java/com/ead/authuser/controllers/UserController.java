package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.services.impl.UserServiceImpl;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.ws.rs.QueryParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {
    final UserService service;
    Logger logger = LogManager.getLogger(UserController.class);
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec, Pageable pageable, @RequestParam(required = false) UUID courseId){
        Page<UserModel> userModelPage = (courseId != null)
                ? service.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable)
                : service.findAll(spec,pageable);
        if (!userModelPage.isEmpty()){
            userModelPage.toList().forEach(
                    userModel -> {
                        userModel.add(
                                linkTo(methodOn(UserController.class).getOneUser(userModel.getUserId())).withSelfRel()
                        );
                    }
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable UUID userId){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(userId).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID userId){
        logger.debug("DELETE deleteUser received userId: {}", userId);
        service.delete(service.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser (@PathVariable UUID userId, @RequestBody @Validated(UserRecordDto.UserView.UserPut.class) @JsonView(UserRecordDto.UserView.UserPut.class) UserRecordDto userRecordDto) {
        logger.debug("PUT updateUser received userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(service.updateUser(userRecordDto,service.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword (@PathVariable UUID userId, @RequestBody @Validated(UserRecordDto.UserView.PasswordPut.class) @JsonView(UserRecordDto.UserView.PasswordPut.class) UserRecordDto userRecordDto) {
        logger.debug("PUT updatePassword received userId: {}", userId);
        var existent = service.findById(userId);
        if(!existent.get().getPassword().equals(userRecordDto.oldpassword())){
            logger.warn("Error: Mismatched old password! userId: {}", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }
        service.updatePassword(userRecordDto,existent.get());
        return ResponseEntity.status(HttpStatus.OK).body("Password updated sucessfully!");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<?> updateImage (@PathVariable UUID userId, @RequestBody @Validated(UserRecordDto.UserView.ImagePut.class) @JsonView(UserRecordDto.UserView.ImagePut.class) UserRecordDto userRecordDto) {
        logger.debug("PUT updateImage received userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(service.updateImage(userRecordDto, service.findById(userId).get()));
    }

}
