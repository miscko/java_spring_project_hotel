package com.miscko.booking_service.controller;

import com.miscko.booking_service.dto.AvailabilityRequest;
import com.miscko.booking_service.dto.BookingRequest;
import com.miscko.booking_service.dto.CancelBookingResponse;
import com.miscko.booking_service.model.Booking;
import com.miscko.booking_service.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) Long clientId) {
        return ResponseEntity.ok(bookingService.getAll(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-availability")
    public ResponseEntity<?> checkAvailability(@Valid @RequestBody AvailabilityRequest request) {
        boolean available = bookingService.isRoomAvailable(
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        Map<String, Object> result = new HashMap<>();
        result.put("roomId", request.getRoomId());
        result.put("available", available);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelBookingResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancel(id));
    }
}