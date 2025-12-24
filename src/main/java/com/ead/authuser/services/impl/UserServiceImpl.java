package com.ead.authuser.services.impl;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository  ) {
        this.userRepository = userRepository;
    }


    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        Optional<UserModel> userModelOptional = userRepository.findById(userId);
        if (userModelOptional.isEmpty()){
            throw new NotFoundException("Error: User not found.");
        }
        return userModelOptional;
    }

    @Override
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public UserModel registerUser(UserRecordDto dto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(dto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByEmailAndUsername(String emailOrUsername) {
        return userRepository.existsByEmailAndUsername(emailOrUsername);
    }

    @Override
    public UserModel updateUser(UserRecordDto userRecordDto, UserModel existent) {
        existent.setFullName(userRecordDto.fullName());
        existent.setPhoneNumber(userRecordDto.phoneNumber());
        existent.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(existent);
    }

    @Override
    public UserModel updatePassword(UserRecordDto userRecordDto, UserModel existent) {
        existent.setPassword(userRecordDto.password());
        existent.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(existent);
    }

    @Override
    public UserModel updateImage(UserRecordDto userRecordDto, UserModel existent) {
        existent.setImageUrl(userRecordDto.imageUrl());
        existent.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(existent);
    }

}
