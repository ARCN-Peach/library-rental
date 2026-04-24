package com.library.rental.domain.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public class Rental {

    private final UUID id;
    private final UUID userId;
    private final UUID bookId;
    private final Instant rentedAt;
    private final Instant dueAt;
    private Instant returnedAt;
    private RentalStatus status;

    private Rental(UUID id, UUID userId, UUID bookId, Instant rentedAt, Instant dueAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.userId = Objects.requireNonNull(userId, "userId is required");
        this.bookId = Objects.requireNonNull(bookId, "bookId is required");
        this.rentedAt = Objects.requireNonNull(rentedAt, "rentedAt is required");
        this.dueAt = Objects.requireNonNull(dueAt, "dueAt is required");
        if (dueAt.isBefore(rentedAt)) {
            throw new IllegalArgumentException("dueAt cannot be before rentedAt");
        }
        this.status = RentalStatus.ACTIVE;
    }

    public static Rental create(UUID id, UUID userId, UUID bookId, Instant rentedAt, int daysLimit) {
        if (daysLimit <= 0) {
            throw new IllegalArgumentException("daysLimit must be positive");
        }
        return new Rental(id, userId, bookId, rentedAt, rentedAt.plus(Duration.ofDays(daysLimit)));
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

    public boolean isActive() {
        return status == RentalStatus.ACTIVE;
    }

    public boolean isOverdue(Instant now) {
        return isActive() && now.isAfter(dueAt);
    }

    public void returnRental(Instant returnedAt) {
        if (!isActive()) {
            throw new IllegalStateException("rental is not active");
        }
        this.returnedAt = Objects.requireNonNull(returnedAt, "returnedAt is required");
        this.status = RentalStatus.RETURNED;
    }

    public void markOverdue(Instant now) {
        if (isOverdue(now)) {
            this.status = RentalStatus.OVERDUE;
        }
    }

    public BigDecimal calculateFine(Instant now, BigDecimal dailyRate) {
        Objects.requireNonNull(now, "now is required");
        Objects.requireNonNull(dailyRate, "dailyRate is required");
        if (!now.isAfter(dueAt)) {
            return BigDecimal.ZERO;
        }

        long overdueDays = ChronoUnit.DAYS.between(dueAt, now);
        if (overdueDays <= 0) {
            overdueDays = 1;
        }
        return dailyRate.multiply(BigDecimal.valueOf(overdueDays));
    }
}