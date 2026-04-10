package com.ead.authuser.services;

import com.ead.authuser.models.UserCourseModel;

import java.util.UUID;

public interface UserCourseService {

    UserCourseModel saveSubscriptionUserInCourse(UserCourseModel userModel);

    boolean existsByCourseId(UUID courseId);

    void deleteAllByCourseId(UUID courseId);
}
