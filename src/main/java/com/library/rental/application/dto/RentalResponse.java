package com.library.rental.application.dto;

import java.time.Instant;
import java.util.UUID;

public record RentalResponse(
        UUID rentalId,
        UUID userId,
        UUID bookId,
        String status,
        Instant rentedAt,
        Instant dueAt,
        Instant returnedAt) {
}