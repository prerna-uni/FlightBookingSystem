package com.flightapp.booking.service;

import com.flightapp.booking.dto.*;
import com.flightapp.booking.feign.FareClient;
import com.flightapp.booking.model.Booking;
import com.flightapp.booking.model.Passenger;
import com.flightapp.booking.repository.BookingRepository;
import com.flightapp.booking.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
	private final BookingRepository bookingRepository;
	private final FareClient fareClient;
	private final PassengerRepository passengerRepository;

	@Autowired
	public BookingService(BookingRepository bookingRepository,
	                      FareClient fareClient,
	                      PassengerRepository passengerRepository) {
	    this.bookingRepository = bookingRepository;
	    this.fareClient = fareClient;
	    this.passengerRepository = passengerRepository;
	}

	public BookingResponse createBooking(BookingRequest request) {
	    FareDTO fareDTO = fareClient.getFare(request.getFlightNumber(), request.getFlightDate());

	    if (fareDTO == null || fareDTO.getFare() == null) {
	        BookingResponse response = new BookingResponse();
	        response.setStatus("FAILED");
	        response.setMessage("Fare not available for the selected flight");
	        return response;
	    }

	    double totalFare = fareDTO.getFare() * request.getPassengers().size();
	    String bookingReference = UUID.randomUUID().toString();

	    Booking booking = new Booking();
	    booking.setFlightNumber(request.getFlightNumber());
	    booking.setFlightDate(request.getFlightDate());
	    booking.setBookingReference(bookingReference);
	    booking.setCheckedIn(false);
	    booking.setPaymentDone(false);

	    booking.setTotalFare(totalFare);

	    List<Passenger> passengers = new ArrayList<>();
	    for (PassengerRequest pr : request.getPassengers()) {
	        Passenger passenger = new Passenger();
	        passenger.setName(pr.getName());
	        passenger.setEmail(pr.getEmail());
	        passenger.setGender(pr.getGender());
	        passenger.setCheckedIn(false);
	        passenger.setBooking(booking);
	        passengers.add(passenger);
	    }

	    booking.setPassengers(passengers);
	    bookingRepository.save(booking);

	    BookingResponse response = new BookingResponse();
	    response.setBookingReference(bookingReference);
	    response.setStatus("SUCCESS");
	    response.setMessage("Booking confirmed. Total fare: ₹" + totalFare);
	    response.setTotalFare(totalFare);
	    response.setPassengers(passengers.stream().map(p -> {
	        PassengerDTO dto = new PassengerDTO();
	        dto.setName(p.getName());
	        dto.setEmail(p.getEmail());
	        dto.setGender(p.getGender());
	        dto.setCheckedIn(p.isCheckedIn());
	        return dto;
	    }).collect(Collectors.toList()));

	    return response;
	}

	public Booking getBookingByReference(String bookingReference) {
	    return bookingRepository.findByBookingReference(bookingReference);
	}
	
	public void updatePaymentStatus(String bookingReference) {
	    Booking booking = bookingRepository.findByBookingReference(bookingReference);
	    if (booking != null) {
	        booking.setPaymentDone(true);
	        bookingRepository.save(booking);
	    } else {
	        throw new RuntimeException("Booking not found for reference: " + bookingReference);
	    }
	}


	public void updateCheckinStatus(BookingCheckinUpdateRequest request) {
	    Booking booking = bookingRepository.findByBookingReference(request.getBookingReference());
	    if (booking == null) {
	        throw new RuntimeException("Booking not found with reference: " + request.getBookingReference());
	    }

	    boolean allCheckedIn = true;
	    boolean passengerFound = false;

	    for (Passenger passenger : booking.getPassengers()) {
	        if (passenger.getName().equalsIgnoreCase(request.getPassengerName())) {
	            passenger.setCheckedIn(true);
	            passengerRepository.save(passenger);
	            passengerFound = true;
	        }
	        if (!passenger.isCheckedIn()) {
	            allCheckedIn = false;
	        }
	    }

	    if (!passengerFound) {
	        throw new RuntimeException("Passenger not found: " + request.getPassengerName());
	    }

	    if (allCheckedIn) {
	        booking.setCheckedIn(true);
	        booking.setCheckinNumber("CHK-" + UUID.randomUUID().toString().substring(0, 8));
	    }

	    bookingRepository.save(booking);
	}

	}

