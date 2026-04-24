package com.library.rental.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateRentalRequest(
        @NotNull UUID userId,
        @NotNull UUID bookId,
        @Positive int daysLimit) {
}