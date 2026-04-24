package com.library.rental.domain.event;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredAt();

    String type();
}