package com.library.rental.interfaces.http;

import com.library.rental.application.dto.CreateRentalRequest;
import com.library.rental.application.dto.RentalResponse;
import com.library.rental.application.usecase.GetRentalUseCase;
import com.library.rental.application.usecase.LendBookUseCase;
import com.library.rental.application.usecase.ListOverdueRentalsUseCase;
import com.library.rental.application.usecase.ListUserRentalsUseCase;
import com.library.rental.application.usecase.ReturnBookUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {

    private final LendBookUseCase lendBookUseCase;
    private final ReturnBookUseCase returnBookUseCase;
    private final GetRentalUseCase getRentalUseCase;
    private final ListUserRentalsUseCase listUserRentalsUseCase;
    private final ListOverdueRentalsUseCase listOverdueRentalsUseCase;

    public RentalController(LendBookUseCase lendBookUseCase,
                            ReturnBookUseCase returnBookUseCase,
                            GetRentalUseCase getRentalUseCase,
                            ListUserRentalsUseCase listUserRentalsUseCase,
                            ListOverdueRentalsUseCase listOverdueRentalsUseCase) {
        this.lendBookUseCase = lendBookUseCase;
        this.returnBookUseCase = returnBookUseCase;
        this.getRentalUseCase = getRentalUseCase;
        this.listUserRentalsUseCase = listUserRentalsUseCase;
        this.listOverdueRentalsUseCase = listOverdueRentalsUseCase;
    }

    @PostMapping
    public ResponseEntity<RentalResponse> lend(@Valid @RequestBody CreateRentalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lendBookUseCase.handle(request));
    }

    @GetMapping("/{rentalId}")
    public RentalResponse getRental(@PathVariable UUID rentalId) {
        return getRentalUseCase.handle(rentalId);
    }

    @PutMapping("/{rentalId}/return")
    public RentalResponse returnRental(@PathVariable UUID rentalId) {
        return returnBookUseCase.handle(rentalId);
    }

    @GetMapping("/users/{userId}")
    public List<RentalResponse> listByUser(@PathVariable UUID userId) {
        return listUserRentalsUseCase.handle(userId);
    }

    @GetMapping("/overdue")
    public List<RentalResponse> listOverdue() {
        return listOverdueRentalsUseCase.handle();
    }
}