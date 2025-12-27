package com.ead.authuser.services;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {


    List<UserModel> findAll();

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userModel);

    UserModel registerUser(UserRecordDto dto);


    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    UserModel updateUser(UserRecordDto userRecordDto, UserModel existent);

    UserModel updatePassword(UserRecordDto userRecordDto, UserModel existent);

    UserModel updateImage(@Valid UserRecordDto userRecordDto, UserModel userModel);
}
