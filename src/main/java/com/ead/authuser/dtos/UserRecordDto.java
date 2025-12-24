package com.ead.authuser.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDto(
        @NotBlank
        @Size(min = 3, max = 15)
        String username,
        @NotBlank
        @Size(min = 10, max = 50)
        String fullName,
        @NotBlank
        @Email
        String email,
        @Size(min = 8, max = 20)
        @NotBlank
        String password,
        @Size(min = 8, max = 20)
        String oldpassword,

        @Size(min = 11, max = 12)
        String phoneNumber,

        @Size(max = 255)
        String imageUrl

) {
}
