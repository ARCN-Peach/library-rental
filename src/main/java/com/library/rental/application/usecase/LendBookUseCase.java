package com.library.rental.application.usecase;

import com.library.rental.application.dto.CreateRentalRequest;
import com.library.rental.application.dto.RentalResponse;
import com.library.rental.application.port.BookInventoryPort;
import com.library.rental.application.port.ClockPort;
import com.library.rental.application.port.DomainEventPublisher;
import com.library.rental.application.port.RentalRepository;
import com.library.rental.domain.event.BookLentEvent;
import com.library.rental.domain.exception.ConflictException;
import com.library.rental.domain.model.Rental;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LendBookUseCase {

    private final RentalRepository rentalRepository;
    private final BookInventoryPort bookInventoryPort;
    private final ClockPort clockPort;
    private final DomainEventPublisher eventPublisher;

    public LendBookUseCase(RentalRepository rentalRepository,
                           BookInventoryPort bookInventoryPort,
                           ClockPort clockPort,
                           DomainEventPublisher eventPublisher) {
        this.rentalRepository = rentalRepository;
        this.bookInventoryPort = bookInventoryPort;
        this.clockPort = clockPort;
        this.eventPublisher = eventPublisher;
    }

    public RentalResponse handle(CreateRentalRequest request) {
        var hasOverdueRental = rentalRepository.findActiveByUserId(request.userId()).stream()
                .anyMatch(rental -> rental.isOverdue(clockPort.now()) || rental.getStatus().name().equals("OVERDUE"));
        if (hasOverdueRental) {
            throw new ConflictException("El usuario tiene un préstamo vencido");
        }

        if (!bookInventoryPort.isAvailable(request.bookId())) {
            throw new ConflictException("El libro no está disponible");
        }

        var now = clockPort.now();
        var rental = Rental.create(UUID.randomUUID(), request.userId(), request.bookId(), now, request.daysLimit());
        bookInventoryPort.reserve(request.bookId());
        rentalRepository.save(rental);
        eventPublisher.publish(new BookLentEvent(rental.getId(), rental.getUserId(), rental.getBookId(), rental.getRentedAt(), rental.getDueAt()));
        return toResponse(rental);
    }

    private static RentalResponse toResponse(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getUserId(),
                rental.getBookId(),
                rental.getStatus().name(),
                rental.getRentedAt(),
                rental.getDueAt(),
                rental.getReturnedAt());
    }
}