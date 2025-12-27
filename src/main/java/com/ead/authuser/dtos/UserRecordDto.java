package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDto(
        @NotBlank(groups = { UserView.RegistrationPost.class }, message = "Username is mandatory")
        @Size(min = 5, max = 50, groups = { UserView.RegistrationPost.class }, message = "Size must be between 5 and 50")
        @JsonView(UserView.RegistrationPost.class)
        String username,
        @NotBlank(groups = { UserView.RegistrationPost.class, UserView.UserPut.class }, message = "Fullname is mandatory")
        @Size(min = 10, max = 50, groups = { UserView.RegistrationPost.class, UserView.UserPut.class }, message = "Size must be between 10 and 50")
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,
        @NotBlank(groups = { UserView.RegistrationPost.class }, message = "Email is mandatory")
        @Email(groups = { UserView.RegistrationPost.class }, message = "Email must be in the expected format")
        @JsonView(UserView.RegistrationPost.class)
        String email,
        @Size(min = 6, max = 20, groups = { UserView.RegistrationPost.class, UserView.PasswordPut.class }, message = "Size must be between 6 and 20")
        @NotBlank(groups = { UserView.RegistrationPost.class, UserView.PasswordPut.class }, message = "Password is mandatory")
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,
        @Size(min = 6, max = 20, groups = { UserView.PasswordPut.class }, message = "Size must be between 6 and 20")
        @NotBlank(groups = { UserView.PasswordPut.class }, message = "Old password is mandatory")
        @JsonView(UserView.PasswordPut.class)
        String oldpassword,

        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber,

        @NotBlank(message = "Image URL is mandatory", groups = { UserView.ImagePut.class })
        @JsonView(UserView.ImagePut.class)
        String imageUrl
) {
        public interface UserView {
                interface RegistrationPost {}
                interface UserPut {}
                interface PasswordPut {}
                interface ImagePut {}
        }
}
