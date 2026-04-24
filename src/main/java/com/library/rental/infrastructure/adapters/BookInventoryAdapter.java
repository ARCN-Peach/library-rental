package com.library.rental.infrastructure.adapters;

import com.library.rental.application.port.BookInventoryPort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BookInventoryAdapter implements BookInventoryPort {

    private final Map<UUID, Integer> availableCopies = new ConcurrentHashMap<>();

    @Override
    public boolean isAvailable(UUID bookId) {
        return availableCopies.getOrDefault(bookId, 1) > 0;
    }

    @Override
    public void reserve(UUID bookId) {
        availableCopies.compute(bookId, (key, copies) -> {
            int currentCopies = copies == null ? 1 : copies;
            if (currentCopies <= 0) {
                throw new IllegalStateException("El libro no tiene stock disponible");
            }
            return currentCopies - 1;
        });
    }

    @Override
    public void release(UUID bookId) {
        availableCopies.merge(bookId, 1, Integer::sum);
    }
}