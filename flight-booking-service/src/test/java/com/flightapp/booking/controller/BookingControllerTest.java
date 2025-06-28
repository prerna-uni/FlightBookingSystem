package com.flightapp.booking.controller;

import com.flightapp.booking.dto.BookingRequest;
import com.flightapp.booking.dto.BookingResponse;
import com.flightapp.booking.dto.BookingCheckinUpdateRequest;
import com.flightapp.booking.dto.PassengerRequest;
import com.flightapp.booking.dto.PassengerDTO;
import com.flightapp.booking.model.Booking;
import com.flightapp.booking.model.Passenger;
import com.flightapp.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingRequest bookingRequest;
    private BookingResponse bookingResponse;
    private Booking booking;
    private BookingCheckinUpdateRequest bookingCheckinUpdateRequest;
    private String bookingReference;

    @BeforeEach
    void setUp() {
        bookingReference = "REF123";

        // Setup PassengerRequest
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setName("John Doe");
        passengerRequest.setEmail("john.doe@example.com");
        passengerRequest.setGender("Male");
        List<PassengerRequest> passengerRequests = new ArrayList<>();
        passengerRequests.add(passengerRequest);

        // Setup BookingRequest
        bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL101");
        bookingRequest.setFlightDate("2025-07-15");
        bookingRequest.setPassengers(passengerRequests);

        // Setup PassengerDTO
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setName("John Doe");
        passengerDTO.setEmail("john.doe@example.com");
        passengerDTO.setGender("Male");
        passengerDTO.setCheckedIn(false);
        List<PassengerDTO> passengerDTOs = new ArrayList<>();
        passengerDTOs.add(passengerDTO);

        // Setup BookingResponse
        bookingResponse = new BookingResponse();
        bookingResponse.setBookingReference(bookingReference);
        bookingResponse.setStatus("CONFIRMED");
        bookingResponse.setMessage("Booking successful");
        bookingResponse.setCheckinStatus("PENDING");
        bookingResponse.setTotalFare(150.00);
        bookingResponse.setPassengers(passengerDTOs);

        // Setup Passenger Model
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setName("John Doe");
        passenger.setEmail("john.doe@example.com");
        passenger.setGender("Male");
        passenger.setCheckedIn(false);
        // Note: setting booking in passenger might create circular dependency if not handled correctly in model,
        // but for test setup of 'Booking' it's fine if 'Booking' then sets its list of 'Passenger'
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);

        // Setup Booking Model
        booking = new Booking();
        booking.setId(101L);
        booking.setBookingReference(bookingReference);
        booking.setFlightNumber("FL101");
        booking.setFlightDate("2025-07-15");
        booking.setCheckedIn(false);
        booking.setCheckinNumber(null); // Or an empty string if that's your default
        booking.setTotalFare(150.00);
        booking.setPaymentDone(false);
        booking.setPassengers(passengers); // Set the list of passengers

        // Link booking to passenger (for completeness if needed, though not strictly required for this test)
        passenger.setBooking(booking);


        // Setup BookingCheckinUpdateRequest
        bookingCheckinUpdateRequest = new BookingCheckinUpdateRequest();
        bookingCheckinUpdateRequest.setBookingReference(bookingReference);
        bookingCheckinUpdateRequest.setPassengerName("John Doe"); // Added as per your DTO
        bookingCheckinUpdateRequest.setCheckedIn(true);
    }

    @Test
    void testCreateBooking() {
        // Given
        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(bookingResponse);

        // When
        BookingResponse response = bookingController.createBooking(bookingRequest);

        // Then
        assertNotNull(response);
        assertEquals(bookingResponse.getBookingReference(), response.getBookingReference());
        assertEquals(bookingResponse.getStatus(), response.getStatus());
        assertEquals(bookingResponse.getTotalFare(), response.getTotalFare());
        assertEquals(bookingResponse.getPassengers().size(), response.getPassengers().size());
        verify(bookingService, times(1)).createBooking(any(BookingRequest.class));
    }

    @Test
    void testGetBooking() {
        // Given
        when(bookingService.getBookingByReference(bookingReference)).thenReturn(booking);

        // When
        Booking retrievedBooking = bookingController.getBooking(bookingReference);

        // Then
        assertNotNull(retrievedBooking);
        assertEquals(booking.getBookingReference(), retrievedBooking.getBookingReference());
        assertEquals(booking.getFlightNumber(), retrievedBooking.getFlightNumber());
        assertEquals(booking.getPassengers().size(), retrievedBooking.getPassengers().size());
        verify(bookingService, times(1)).getBookingByReference(bookingReference);
    }

    @Test
    void testUpdateCheckinStatus() {
        // Given
        doNothing().when(bookingService).updateCheckinStatus(any(BookingCheckinUpdateRequest.class));

        // When
        bookingController.updateCheckinStatus(bookingCheckinUpdateRequest);

        // Then
        verify(bookingService, times(1)).updateCheckinStatus(any(BookingCheckinUpdateRequest.class));
    }

    @Test
    void testUpdatePaymentStatus() {
        // Given
        doNothing().when(bookingService).updatePaymentStatus(bookingReference);

        // When
        ResponseEntity<String> response = bookingController.updatePaymentStatus(bookingReference);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Payment status updated", response.getBody());
        verify(bookingService, times(1)).updatePaymentStatus(bookingReference);
    }
}