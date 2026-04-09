package com.ead.authuser.validations;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    Logger logger = LogManager.getLogger(UserValidator.class);

    private final Validator validator;
    private final UserServiceImpl userService;

    public UserValidator(@Qualifier("defaultValidator") Validator validator, UserServiceImpl userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRecordDto userRecordDto = (UserRecordDto) o;
        validator.validate(userRecordDto,errors);
        if (!errors.hasErrors()){
            validateEmail(userRecordDto,errors);
            validateUsername(userRecordDto, errors);
        }
    }

    private void validateEmail(UserRecordDto userRecordDto, Errors errors){
        if (userService.existsByEmail(userRecordDto.email())){
            errors.rejectValue("email", "UserEmailError", "Email has been taken!");
            logger.error("Error Validator email: {}", userRecordDto.email());
        }
    }

    private void validateUsername(UserRecordDto userRecordDto, Errors errors){
        if (userService.existsByUsername(userRecordDto.username())){
            errors.rejectValue("username", "UserNameError", "Username has been taken!");
            logger.error("Error Validator username: {}", userRecordDto.username());
        }
    }
}
