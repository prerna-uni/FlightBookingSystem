package com.flightapp.booking.service;

import com.flightapp.booking.dto.BookingRequest;
import com.flightapp.booking.dto.BookingResponse;
import com.flightapp.booking.dto.FareDTO;
import com.flightapp.booking.feign.FareClient;
import com.flightapp.booking.model.Booking;
import com.flightapp.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FareClient fareClient;

    @Autowired
    public BookingService(BookingRepository bookingRepository, FareClient fareClient) {
        this.bookingRepository = bookingRepository;
        this.fareClient = fareClient;
    }

    public BookingResponse createBooking(BookingRequest request) {
        // Call Fare Service to get fare
        FareDTO fareDTO = fareClient.getFare(request.getFlightNumber(), request.getFlightDate());
        if (fareDTO == null || fareDTO.getFare() == null) {
            BookingResponse response = new BookingResponse();
            response.setStatus("FAILED");
            response.setMessage("Fare not available for the selected flight");
            return response;
        }

        // Create booking reference
        String bookingReference = UUID.randomUUID().toString();

        // Save booking in DB
        Booking booking = new Booking();
        booking.setFlightNumber(request.getFlightNumber());
        booking.setFlightDate(request.getFlightDate());
        booking.setPassengerName(request.getPassengerName());
        booking.setNumberOfSeats(request.getNumberOfSeats());
        booking.setBookingReference(bookingReference);

        bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();
        response.setBookingReference(bookingReference);
        response.setStatus("SUCCESS");
        response.setMessage("Booking confirmed with fare: " + fareDTO.getFare());

        return response;
    }

    public Booking getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference);
    }
}
