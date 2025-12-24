package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDto(
        @NotBlank
        @Size(min = 3, max = 15)
        @JsonView(UserView.RegistrationPost.class)
        String username,
        @NotBlank
        @Size(min = 10, max = 50)
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,
        @NotBlank
        @Email
        @JsonView(UserView.RegistrationPost.class)
        String email,
        @Size(min = 8, max = 20)
        @NotBlank
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,
        @Size(min = 8, max = 20)
        @NotBlank
        @JsonView(UserView.PasswordPut.class)
        String oldpassword,

        @Size(min = 11, max = 12)
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber,

        @Size(max = 255)
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
