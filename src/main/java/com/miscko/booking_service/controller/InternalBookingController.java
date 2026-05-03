package com.miscko.booking_service.controller;

import com.miscko.booking_service.dto.BookingInfoResponse;
import com.miscko.booking_service.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/bookings")
public class InternalBookingController {

    private final BookingService bookingService;

    public InternalBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingInfoResponse> getBookingInfo(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingInfo(id));
    }
}