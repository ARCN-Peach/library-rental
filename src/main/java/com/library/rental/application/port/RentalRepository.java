package com.library.rental.application.port;

import com.library.rental.domain.model.Rental;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalRepository {

    Rental save(Rental rental);

    Optional<Rental> findById(UUID id);

    List<Rental> findByUserId(UUID userId);

    List<Rental> findActiveByUserId(UUID userId);

    List<Rental> findOverdue();

    List<Rental> findAll();
}