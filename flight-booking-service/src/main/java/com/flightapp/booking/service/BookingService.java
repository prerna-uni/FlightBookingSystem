package com.flightapp.booking.service;

import com.flightapp.booking.dto.BookingCheckinUpdateRequest;
import com.flightapp.booking.dto.BookingDTO;
import com.flightapp.booking.dto.BookingRequest;
import com.flightapp.booking.dto.BookingResponse;
import com.flightapp.booking.dto.FareDTO;
import com.flightapp.booking.feign.FareClient;
import com.flightapp.booking.model.Booking;
import com.flightapp.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	    
	    FareDTO fareDTO = fareClient.getFare(request.getFlightNumber(), request.getFlightDate());
	    if (fareDTO == null || fareDTO.getFare() == null) {
	        BookingResponse response = new BookingResponse();
	        response.setStatus("FAILED");
	        response.setMessage("Fare not available for the selected flight");
	        return response;
	    }

	    
	    double totalFare = fareDTO.getFare() * request.getNumberOfSeats();

	    
	    String bookingReference = UUID.randomUUID().toString();

	    
	    Booking booking = new Booking();
	    booking.setFlightNumber(request.getFlightNumber());
	    booking.setFlightDate(request.getFlightDate());
	    booking.setPassengerName(request.getPassengerName());
	    booking.setNumberOfSeats(request.getNumberOfSeats());
	    booking.setBookingReference(bookingReference);
	    booking.setCheckedIn(false); // initial check-in status
	    booking.setTotalFare(totalFare);

	    bookingRepository.save(booking);

	    BookingResponse response = new BookingResponse();
	    response.setBookingReference(bookingReference);
	    response.setStatus("SUCCESS");
	    response.setMessage("Booking confirmed. Total fare: ₹" + totalFare);
	    response.setTotalFare(totalFare);

	    return response;
	}
	public Booking getBookingByReference(String bookingReference) {
	    return bookingRepository.findByBookingReference(bookingReference);
	}

	public void updateCheckinStatus(BookingCheckinUpdateRequest bookingCheckinUpdateRequest) {
	    Booking booking = bookingRepository.findByBookingReference(bookingCheckinUpdateRequest.getBookingReference());
	    if (booking != null) {
	        booking.setCheckedIn(true);
	        booking.setCheckinNumber(bookingCheckinUpdateRequest.getCheckinNumber());
	        bookingRepository.save(booking);
	    } else {
	        throw new RuntimeException("Booking not found with reference: " + bookingCheckinUpdateRequest.getBookingReference());
	    }
	}


	
}