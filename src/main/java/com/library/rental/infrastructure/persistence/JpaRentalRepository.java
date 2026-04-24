package com.library.rental.infrastructure.persistence;

import com.library.rental.application.port.ClockPort;
import com.library.rental.application.port.RentalRepository;
import com.library.rental.domain.model.Rental;
import com.library.rental.domain.model.RentalStatus;
import com.library.rental.infrastructure.persistence.jpa.RentalJpaEntity;
import com.library.rental.infrastructure.persistence.jpa.SpringDataRentalJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class JpaRentalRepository implements RentalRepository {

    private final SpringDataRentalJpaRepository springDataRentalJpaRepository;
    private final ClockPort clockPort;

    public JpaRentalRepository(SpringDataRentalJpaRepository springDataRentalJpaRepository, ClockPort clockPort) {
        this.springDataRentalJpaRepository = springDataRentalJpaRepository;
        this.clockPort = clockPort;
    }

    @Override
    public Rental save(Rental rental) {
        var saved = springDataRentalJpaRepository.save(RentalJpaEntity.fromDomain(rental));
        return saved.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rental> findById(UUID id) {
        return springDataRentalJpaRepository.findById(id).map(RentalJpaEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> findByUserId(UUID userId) {
        return springDataRentalJpaRepository.findByUserId(userId).stream().map(RentalJpaEntity::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> findActiveByUserId(UUID userId) {
        return springDataRentalJpaRepository.findByUserIdAndStatus(userId, RentalStatus.ACTIVE)
                .stream()
                .map(RentalJpaEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> findOverdue() {
        Instant now = clockPort.now();
        return springDataRentalJpaRepository.findAll().stream()
                .map(RentalJpaEntity::toDomain)
                .filter(rental -> rental.isOverdue(now) || rental.getStatus() == RentalStatus.OVERDUE)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> findAll() {
        return springDataRentalJpaRepository.findAll().stream().map(RentalJpaEntity::toDomain).toList();
    }
}