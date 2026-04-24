package com.library.rental.application.usecase;

import com.library.rental.application.dto.RentalResponse;
import com.library.rental.application.port.BookInventoryPort;
import com.library.rental.application.port.ClockPort;
import com.library.rental.application.port.DomainEventPublisher;
import com.library.rental.application.port.RentalRepository;
import com.library.rental.domain.event.BookReturnedEvent;
import com.library.rental.domain.exception.NotFoundException;
import com.library.rental.domain.model.Rental;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ReturnBookUseCase {

    private final RentalRepository rentalRepository;
    private final BookInventoryPort bookInventoryPort;
    private final ClockPort clockPort;
    private final DomainEventPublisher eventPublisher;

    public ReturnBookUseCase(RentalRepository rentalRepository,
                             BookInventoryPort bookInventoryPort,
                             ClockPort clockPort,
                             DomainEventPublisher eventPublisher) {
        this.rentalRepository = rentalRepository;
        this.bookInventoryPort = bookInventoryPort;
        this.clockPort = clockPort;
        this.eventPublisher = eventPublisher;
    }

    public RentalResponse handle(UUID rentalId) {
        var rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new NotFoundException("No existe el préstamo solicitado"));

        var now = clockPort.now();
        rental.returnRental(now);
        rentalRepository.save(rental);
        bookInventoryPort.release(rental.getBookId());

        eventPublisher.publish(new BookReturnedEvent(rental.getId(), rental.getUserId(), rental.getBookId(), now));
        return new RentalResponse(rental.getId(), rental.getUserId(), rental.getBookId(), rental.getStatus().name(), rental.getRentedAt(), rental.getDueAt(), rental.getReturnedAt());
    }
}