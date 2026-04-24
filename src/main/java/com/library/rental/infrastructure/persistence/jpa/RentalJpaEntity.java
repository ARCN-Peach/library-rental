package com.library.rental.infrastructure.persistence.jpa;

import com.library.rental.domain.model.Rental;
import com.library.rental.domain.model.RentalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rentals")
public class RentalJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "book_id", nullable = false)
    private UUID bookId;

    @Column(name = "rented_at", nullable = false)
    private Instant rentedAt;

    @Column(name = "due_at", nullable = false)
    private Instant dueAt;

    @Column(name = "returned_at")
    private Instant returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    protected RentalJpaEntity() {
    }

    private RentalJpaEntity(UUID id, UUID userId, UUID bookId, Instant rentedAt, Instant dueAt, Instant returnedAt, RentalStatus status) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.rentedAt = rentedAt;
        this.dueAt = dueAt;
        this.returnedAt = returnedAt;
        this.status = status;
    }

    public static RentalJpaEntity fromDomain(Rental rental) {
        return new RentalJpaEntity(
                rental.getId(),
                rental.getUserId(),
                rental.getBookId(),
                rental.getRentedAt(),
                rental.getDueAt(),
                rental.getReturnedAt(),
                rental.getStatus());
    }

    public Rental toDomain() {
        var rental = Rental.create(id, userId, bookId, rentedAt, (int) java.time.temporal.ChronoUnit.DAYS.between(rentedAt, dueAt));
        if (status == RentalStatus.RETURNED && returnedAt != null) {
            rental.returnRental(returnedAt);
        } else if (status == RentalStatus.OVERDUE) {
            rental.markOverdue(dueAt.plusSeconds(1));
        }
        return rental;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public Instant getRentedAt() {
        return rentedAt;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public Instant getReturnedAt() {
        return returnedAt;
    }

    public RentalStatus getStatus() {
        return status;
    }
}