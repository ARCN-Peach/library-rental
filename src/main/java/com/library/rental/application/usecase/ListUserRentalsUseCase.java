package com.library.rental.application.usecase;

import com.library.rental.application.dto.RentalResponse;
import com.library.rental.application.port.RentalRepository;
import com.library.rental.domain.model.Rental;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListUserRentalsUseCase {

    private final RentalRepository rentalRepository;

    public ListUserRentalsUseCase(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<RentalResponse> handle(UUID userId) {
        return rentalRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    private RentalResponse toResponse(Rental rental) {
        return new RentalResponse(rental.getId(), rental.getUserId(), rental.getBookId(), rental.getStatus().name(), rental.getRentedAt(), rental.getDueAt(), rental.getReturnedAt());
    }
}