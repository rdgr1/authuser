package com.ead.authuser.controllers;

import com.ead.authuser.client.CourseClient;
import com.ead.authuser.dtos.CourseRecordDto;
import com.ead.authuser.dtos.SubscriptionRecordDto;
import com.ead.authuser.services.impl.UserCourseServiceImpl;
import com.ead.authuser.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserCourseController {
    final CourseClient client;
    final UserCourseServiceImpl service;
    final UserServiceImpl userService;

    public UserCourseController(CourseClient client, UserCourseServiceImpl service, UserServiceImpl userService) {
        this.client = client;
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/users/{userId}/courses/")
    public ResponseEntity<Page<CourseRecordDto>> getAllCoursesByUser(@PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID userId){
        userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(client.getAllCoursesByUser(userId,pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID userId, @Valid @RequestBody SubscriptionRecordDto subscriptionRecordDto){
        // User Verify
        var user = userService.findById(userId);
        //Subscription Validate
        if (Boolean.TRUE.equals(client.existsByCourseIdAndUser(subscriptionRecordDto.courseId(), user.get()).getBody())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveSubscriptionUserInCourse(user.get().convertToUserCouserModel(subscriptionRecordDto.courseId(), user.get())));
    }

    @DeleteMapping("/users/courses/{courseId}")
    public ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable UUID courseId){
        if (!service.existsByCourseId(courseId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserCourse Not Found!");
        }
        service.deleteAllByCourseId(courseId);
        return ResponseEntity.status(HttpStatus.OK).body("UserCourse deleted successfully!");
    }
}
