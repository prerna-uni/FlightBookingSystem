package com.flightapp.booking.service;

import com.flightapp.booking.dto.*;
import com.flightapp.booking.feign.FareClient;
import com.flightapp.booking.model.Booking;
import com.flightapp.booking.model.Passenger;
import com.flightapp.booking.repository.BookingRepository;
import com.flightapp.booking.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections; // Import for Collections.emptyList()
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FareClient fareClient;

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private BookingService bookingService;

    // Common test data
    private String bookingReference;
    private BookingRequest bookingRequest;
    private FareDTO fareDTO;
    private Booking booking;
    private Passenger passenger1;
    private Passenger passenger2;
    private BookingCheckinUpdateRequest checkinRequest;

    @BeforeEach
    void setUp() {
        bookingReference = UUID.randomUUID().toString(); // Use UUID for unique reference

        // 1. Setup PassengerRequest
        PassengerRequest passengerRequest1 = new PassengerRequest();
        passengerRequest1.setName("John Doe");
        passengerRequest1.setEmail("john.doe@example.com");
        passengerRequest1.setGender("Male");

        PassengerRequest passengerRequest2 = new PassengerRequest();
        passengerRequest2.setName("Jane Smith");
        passengerRequest2.setEmail("jane.smith@example.com");
        passengerRequest2.setGender("Female");

        List<PassengerRequest> passengerRequests = new ArrayList<>();
        passengerRequests.add(passengerRequest1);
        passengerRequests.add(passengerRequest2);

        // 2. Setup BookingRequest
        bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL101");
        bookingRequest.setFlightDate("2025-07-15");
        bookingRequest.setPassengers(passengerRequests);

        // 3. Setup FareDTO
        fareDTO = new FareDTO();
        fareDTO.setFare(100.00); // Sample fare

        // 4. Setup Passenger Model (for existing booking)
        passenger1 = new Passenger();
        passenger1.setId(1L);
        passenger1.setName("John Doe");
        passenger1.setEmail("john.doe@example.com");
        passenger1.setGender("Male");
        passenger1.setCheckedIn(false);

        passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setName("Jane Smith");
        passenger2.setEmail("jane.smith@example.com");
        passenger2.setGender("Female");
        passenger2.setCheckedIn(false);

        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger1);
        passengers.add(passenger2);

        // 5. Setup Booking Model
        booking = new Booking();
        booking.setId(101L);
        booking.setFlightNumber("FL101");
        booking.setFlightDate("2025-07-15");
        booking.setBookingReference(bookingReference);
        booking.setCheckedIn(false);
        booking.setPaymentDone(false);
        booking.setTotalFare(200.00); // 2 passengers * 100.00 fare
        booking.setPassengers(passengers);

        // Link passengers to booking
        passenger1.setBooking(booking);
        passenger2.setBooking(booking);

        // 6. Setup BookingCheckinUpdateRequest
        checkinRequest = new BookingCheckinUpdateRequest();
        checkinRequest.setBookingReference(bookingReference);
        checkinRequest.setPassengerName("John Doe");
        checkinRequest.setCheckedIn(true);
    }



    @Test
    void testCreateBooking_Success() {
        // Given
        when(fareClient.getFare(bookingRequest.getFlightNumber(), bookingRequest.getFlightDate())).thenReturn(fareDTO);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // When
        BookingResponse response = bookingService.createBooking(bookingRequest);

        // Then
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getBookingReference()); // UUID generated
        assertEquals(bookingRequest.getPassengers().size() * fareDTO.getFare(), response.getTotalFare());
        assertEquals(bookingRequest.getPassengers().size(), response.getPassengers().size());

        // Verify interactions
        verify(fareClient, times(1)).getFare(bookingRequest.getFlightNumber(), bookingRequest.getFlightDate());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(passengerRepository, never()).save(any(Passenger.class)); // Ensures passengerRepo.save was NOT called
    }

    @Test
    void testCreateBooking_FareNotAvailable_ReturnsFailedResponse() {
        // Given: FareDTO is null
        when(fareClient.getFare(bookingRequest.getFlightNumber(), bookingRequest.getFlightDate())).thenReturn(null);

        // When
        BookingResponse response = bookingService.createBooking(bookingRequest);

        // Then
        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
        assertEquals("Fare not available for the selected flight", response.getMessage());
        assertNull(response.getBookingReference()); // Should be null as booking isn't created

        // Verify no booking or passenger save attempts
        verify(bookingRepository, never()).save(any(Booking.class));
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void testCreateBooking_FareValueNull_ReturnsFailedResponse() {
        // Given: FareDTO has null fare value
        fareDTO.setFare(null); // Set fare to null
        when(fareClient.getFare(bookingRequest.getFlightNumber(), bookingRequest.getFlightDate())).thenReturn(fareDTO);

        // When
        BookingResponse response = bookingService.createBooking(bookingRequest);

        // Then
        assertNotNull(response);
        assertEquals("FAILED", response.getStatus());
        assertEquals("Fare not available for the selected flight", response.getMessage());
        assertNull(response.getBookingReference());

        // Verify no booking or passenger save attempts
        verify(bookingRepository, never()).save(any(Booking.class));
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void testCreateBooking_NoPassengers_Success() {
        // Given
        BookingRequest requestWithNoPassengers = new BookingRequest();
        requestWithNoPassengers.setFlightNumber("FL101");
        requestWithNoPassengers.setFlightDate("2025-07-15");
        requestWithNoPassengers.setPassengers(Collections.emptyList()); // Set empty list

        // Mock the FareClient to return a valid fare
        when(fareClient.getFare(requestWithNoPassengers.getFlightNumber(), requestWithNoPassengers.getFlightDate())).thenReturn(fareDTO);
        when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking()); // Return a minimal booking object

        // When
        BookingResponse response = bookingService.createBooking(requestWithNoPassengers);

        // Then
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getBookingReference());
        assertEquals(0.0, response.getTotalFare()); // Fare should be 0 for 0 passengers
        assertTrue(response.getPassengers().isEmpty()); // No passengers in response

        verify(fareClient, times(1)).getFare(requestWithNoPassengers.getFlightNumber(), requestWithNoPassengers.getFlightDate());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(passengerRepository, never()).save(any(Passenger.class));
    }




    @Test
    void testGetBookingByReference_Found() {
        // Given
        when(bookingRepository.findByBookingReference(bookingReference)).thenReturn(booking);

        // When
        Booking foundBooking = bookingService.getBookingByReference(bookingReference);

        // Then
        assertNotNull(foundBooking);
        assertEquals(bookingReference, foundBooking.getBookingReference());
        verify(bookingRepository, times(1)).findByBookingReference(bookingReference);
    }

    @Test
    void testGetBookingByReference_NotFound() {
        // Given
        when(bookingRepository.findByBookingReference("NON_EXISTENT_REF")).thenReturn(null);

        // When
        Booking foundBooking = bookingService.getBookingByReference("NON_EXISTENT_REF");

        // Then
        assertNull(foundBooking);
        verify(bookingRepository, times(1)).findByBookingReference("NON_EXISTENT_REF");
    }



    @Test
    void testUpdatePaymentStatus_Success() {
        // Given
        when(bookingRepository.findByBookingReference(bookingReference)).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking); // Return the saved booking

        // Pre-condition check
        assertFalse(booking.isPaymentDone());

        // When
        bookingService.updatePaymentStatus(bookingReference);

        // Then
        assertTrue(booking.isPaymentDone()); // Verify the status was changed on the object
        verify(bookingRepository, times(1)).findByBookingReference(bookingReference);
        verify(bookingRepository, times(1)).save(booking); // Verify save was called with the modified booking
    }

    @Test
    void testUpdatePaymentStatus_BookingNotFound_ThrowsException() {
        // Given
        when(bookingRepository.findByBookingReference("NON_EXISTENT_REF")).thenReturn(null);

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bookingService.updatePaymentStatus("NON_EXISTENT_REF")
        );
        assertEquals("Booking not found for reference: NON_EXISTENT_REF", exception.getMessage());
        verify(bookingRepository, times(1)).findByBookingReference("NON_EXISTENT_REF");
        verify(bookingRepository, never()).save(any(Booking.class)); // Ensure no save was attempted
    }



    @Test
    void testUpdateCheckinStatus_SinglePassenger_UpdatesCheckin() {
        // Given
        // Initial state: passenger1 is not checked in
        assertFalse(passenger1.isCheckedIn());
        assertFalse(booking.isCheckedIn());
        assertNull(booking.getCheckinNumber());

        when(bookingRepository.findByBookingReference(bookingReference)).thenReturn(booking);
        // Mock save for passenger, it will be called for passenger1
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger1);
        // Mock save for booking
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // When
        bookingService.updateCheckinStatus(checkinRequest);

        // Then
        assertTrue(passenger1.isCheckedIn()); // John Doe should be checked in
        assertFalse(passenger2.isCheckedIn()); // Jane Smith should still be unchecked
        assertFalse(booking.isCheckedIn()); // Booking should still be unchecked (not all checked in)
        assertNull(booking.getCheckinNumber()); // No checkin number generated yet

        verify(bookingRepository, times(1)).findByBookingReference(bookingReference);
        verify(passengerRepository, times(1)).save(passenger1); // Only passenger1 is saved
        verify(bookingRepository, times(1)).save(booking); // Booking is saved at the end
    }

    @Test
    void testUpdateCheckinStatus_AllPassengersCheckedIn_UpdatesBooking() {
        // Given
        // Set both passengers to initially not checked in
        passenger1.setCheckedIn(false);
        passenger2.setCheckedIn(false);
        booking.setCheckedIn(false);
        booking.setCheckinNumber(null);

        // Create a check-in request for the second passenger
        BookingCheckinUpdateRequest checkinRequestJane = new BookingCheckinUpdateRequest();
        checkinRequestJane.setBookingReference(bookingReference);
        checkinRequestJane.setPassengerName("Jane Smith");
        checkinRequestJane.setCheckedIn(true);

        when(bookingRepository.findByBookingReference(bookingReference)).thenReturn(booking);
        // Mock saves for both passengers
        // First call to save passenger is for John Doe, second for Jane Smith
        when(passengerRepository.save(any(Passenger.class)))
            .thenReturn(passenger1) // For first call (John Doe)
            .thenReturn(passenger2); // For second call (Jane Smith)
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // First check-in: John Doe
        bookingService.updateCheckinStatus(checkinRequest);
        // Now John is checked in, Jane is not

        // Second check-in: Jane Smith (this should make all checked in)
        bookingService.updateCheckinStatus(checkinRequestJane);

        // Then
        assertTrue(passenger1.isCheckedIn()); // John Doe should be checked in
        assertTrue(passenger2.isCheckedIn()); // Jane Smith should be checked in
        assertTrue(booking.isCheckedIn()); // Booking should now be checked in
        assertNotNull(booking.getCheckinNumber()); // Checkin number should be generated
        assertTrue(booking.getCheckinNumber().startsWith("CHK-"));

        verify(bookingRepository, times(2)).findByBookingReference(bookingReference); // Called twice
        verify(passengerRepository, times(2)).save(any(Passenger.class)); // Both passengers saved
        verify(bookingRepository, times(2)).save(booking); // Booking saved after each check-in (once to update passenger list, once to update booking status)
    }

    @Test
    void testUpdateCheckinStatus_BookingNotFound_ThrowsException() {
        // Given
        when(bookingRepository.findByBookingReference("NON_EXISTENT_REF")).thenReturn(null);
        BookingCheckinUpdateRequest nonExistentBookingRequest = new BookingCheckinUpdateRequest();
        nonExistentBookingRequest.setBookingReference("NON_EXISTENT_REF");
        nonExistentBookingRequest.setPassengerName("Any Passenger");
        nonExistentBookingRequest.setCheckedIn(true);

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bookingService.updateCheckinStatus(nonExistentBookingRequest)
        );
        assertEquals("Booking not found with reference: NON_EXISTENT_REF", exception.getMessage());
        verify(bookingRepository, times(1)).findByBookingReference("NON_EXISTENT_REF");
        verify(passengerRepository, never()).save(any(Passenger.class)); // Ensure no save was attempted
        verify(bookingRepository, never()).save(any(Booking.class)); // Ensure no save was attempted
    }

    @Test
    void testUpdateCheckinStatus_PassengerNotFound_ThrowsException() {
        // Given
        when(bookingRepository.findByBookingReference(bookingReference)).thenReturn(booking);
        BookingCheckinUpdateRequest nonExistentPassengerRequest = new BookingCheckinUpdateRequest();
        nonExistentPassengerRequest.setBookingReference(bookingReference);
        nonExistentPassengerRequest.setPassengerName("Non Existent Passenger");
        nonExistentPassengerRequest.setCheckedIn(true);

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bookingService.updateCheckinStatus(nonExistentPassengerRequest)
        );
        assertEquals("Passenger not found: Non Existent Passenger", exception.getMessage());
        verify(bookingRepository, times(1)).findByBookingReference(bookingReference);
        verify(passengerRepository, never()).save(any(Passenger.class)); // Ensure no passenger save was attempted
        verify(bookingRepository, never()).save(any(Booking.class)); // Ensure no booking save was attempted if passenger not found before all checked in logic
    }
}