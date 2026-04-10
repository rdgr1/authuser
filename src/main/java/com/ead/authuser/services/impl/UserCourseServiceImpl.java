package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    final UserCourseRepository repo;
    final UserServiceImpl service;

    public UserCourseServiceImpl(UserCourseRepository repo, UserServiceImpl service) {
        this.repo = repo;
        this.service = service;
    }

    @Override
    public UserCourseModel saveSubscriptionUserInCourse(UserCourseModel userModel) {
        return repo.save(userModel);
    }

    @Override
    public boolean existsByCourseId(UUID courseId) {
        return repo.existsByCourseId(courseId);
    }

    @Transactional
    @Override
    public void deleteAllByCourseId(UUID courseId) {
        if (existsByCourseId(courseId)){
            repo.deleteAllByCourseId(courseId);
        }
    }


}
