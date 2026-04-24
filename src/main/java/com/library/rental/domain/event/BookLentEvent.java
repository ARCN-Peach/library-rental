package com.library.rental.domain.event;

import java.time.Instant;
import java.util.UUID;

public record BookLentEvent(UUID rentalId, UUID userId, UUID bookId, Instant rentedAt, Instant dueAt)
        implements DomainEvent {

    @Override
    public Instant occurredAt() {
        return rentedAt;
    }

    @Override
    public String type() {
        return "rental.book.lent.v1";
    }
}