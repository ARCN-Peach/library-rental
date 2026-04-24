package com.library.rental.application.port;

import com.library.rental.domain.event.DomainEvent;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}