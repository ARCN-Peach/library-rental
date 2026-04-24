package com.library.rental.application.usecase;

import com.library.rental.application.dto.RentalResponse;
import com.library.rental.application.port.RentalRepository;
import com.library.rental.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetRentalUseCase {

    private final RentalRepository rentalRepository;

    public GetRentalUseCase(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public RentalResponse handle(UUID rentalId) {
        var rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new NotFoundException("No existe el préstamo solicitado"));
        return new RentalResponse(rental.getId(), rental.getUserId(), rental.getBookId(), rental.getStatus().name(), rental.getRentedAt(), rental.getDueAt(), rental.getReturnedAt());
    }
}