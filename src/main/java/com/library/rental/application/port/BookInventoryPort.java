package com.library.rental.application.port;

import java.util.UUID;

public interface BookInventoryPort {

    boolean isAvailable(UUID bookId);

    void reserve(UUID bookId);

    void release(UUID bookId);
}