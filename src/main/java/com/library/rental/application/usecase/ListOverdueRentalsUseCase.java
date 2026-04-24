package com.library.rental.application.usecase;

import com.library.rental.application.dto.RentalResponse;
import com.library.rental.application.port.ClockPort;
import com.library.rental.application.port.RentalRepository;
import com.library.rental.domain.model.Rental;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListOverdueRentalsUseCase {

    private final RentalRepository rentalRepository;
    private final ClockPort clockPort;

    public ListOverdueRentalsUseCase(RentalRepository rentalRepository, ClockPort clockPort) {
        this.rentalRepository = rentalRepository;
        this.clockPort = clockPort;
    }

    public List<RentalResponse> handle() {
        var now = clockPort.now();
        return rentalRepository.findOverdue().stream()
                .filter(rental -> rental.isOverdue(now) || rental.getStatus().name().equals("OVERDUE"))
                .map(this::toResponse)
                .toList();
    }

    private RentalResponse toResponse(Rental rental) {
        return new RentalResponse(rental.getId(), rental.getUserId(), rental.getBookId(), rental.getStatus().name(), rental.getRentedAt(), rental.getDueAt(), rental.getReturnedAt());
    }
}