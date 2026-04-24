package com.library.rental.domain.event;

import java.time.Instant;
import java.util.UUID;

public record BookReturnedEvent(UUID rentalId, UUID userId, UUID bookId, Instant returnedAt)
        implements DomainEvent {

    @Override
    public Instant occurredAt() {
        return returnedAt;
    }

    @Override
    public String type() {
        return "rental.book.returned.v1";
    }
}