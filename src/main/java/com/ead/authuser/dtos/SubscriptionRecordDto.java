package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubscriptionRecordDto(
        @NotNull(message = "userId is mandatory!")
        UUID userId,
        UUID courseId
) {
}
