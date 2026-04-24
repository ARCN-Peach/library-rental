package com.library.rental.application.usecase;

import com.library.rental.application.dto.CreateRentalRequest;
import com.library.rental.application.port.BookInventoryPort;
import com.library.rental.application.port.ClockPort;
import com.library.rental.application.port.DomainEventPublisher;
import com.library.rental.application.port.RentalRepository;
import com.library.rental.domain.event.BookLentEvent;
import com.library.rental.domain.model.Rental;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LendBookUseCaseTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private BookInventoryPort bookInventoryPort;

    @Mock
    private ClockPort clockPort;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private LendBookUseCase lendBookUseCase;

    @Test
    void shouldCreateRentalWhenBookIsAvailableAndUserHasNoOverdueLoans() {
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var bookId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        var rentedAt = Instant.parse("2026-04-24T10:00:00Z");

        when(rentalRepository.findActiveByUserId(userId)).thenReturn(List.of());
        when(bookInventoryPort.isAvailable(bookId)).thenReturn(true);
        when(clockPort.now()).thenReturn(rentedAt);
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = lendBookUseCase.handle(new CreateRentalRequest(userId, bookId, 7));
        var eventCaptor = ArgumentCaptor.forClass(BookLentEvent.class);

        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.bookId()).isEqualTo(bookId);
        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(response.rentedAt()).isEqualTo(rentedAt);
        assertThat(response.dueAt()).isEqualTo(Instant.parse("2026-05-01T10:00:00Z"));

        verify(bookInventoryPort).reserve(bookId);
        verify(eventPublisher).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue().type()).isEqualTo("rental.book.lent.v1");
        assertThat(eventCaptor.getValue().userId()).isEqualTo(userId);
        assertThat(eventCaptor.getValue().bookId()).isEqualTo(bookId);
        assertThat(eventCaptor.getValue().rentedAt()).isEqualTo(rentedAt);
    }
}