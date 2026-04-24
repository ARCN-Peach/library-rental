package com.library.rental.infrastructure.persistence.jpa;

import com.library.rental.domain.model.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataRentalJpaRepository extends JpaRepository<RentalJpaEntity, UUID> {

    List<RentalJpaEntity> findByUserId(UUID userId);

    List<RentalJpaEntity> findByUserIdAndStatus(UUID userId, RentalStatus status);

    List<RentalJpaEntity> findByStatus(RentalStatus status);
}