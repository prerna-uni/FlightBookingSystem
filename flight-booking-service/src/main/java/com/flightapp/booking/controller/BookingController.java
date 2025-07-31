package com.flightapp.booking.controller;

import com.flightapp.booking.dto.BookingRequest;

import com.flightapp.booking.dto.BookingResponse;
import com.flightapp.booking.dto.BookingCheckinUpdateRequest;
import com.flightapp.booking.model.Booking;
import com.flightapp.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponse createBooking(@RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/{bookingReference}")
    public Booking getBooking(@PathVariable String bookingReference) {
        return bookingService.getBookingByReference(bookingReference);
    }
    
    @PutMapping("/checkin")
    public void updateCheckinStatus(@RequestBody BookingCheckinUpdateRequest bookingCheckinUpdateRequest) {
        bookingService.updateCheckinStatus(bookingCheckinUpdateRequest);
    }
    
    @PutMapping("/payment-confirmed/{bookingReference}")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable String bookingReference) {
        bookingService.updatePaymentStatus(bookingReference);
        return ResponseEntity.ok("Payment status updated");
    }


}