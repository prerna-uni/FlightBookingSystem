package com.flightapp.booking.service;

import com.flightapp.booking.dto.*;
import com.flightapp.booking.feign.FareClient;
import com.flightapp.booking.model.Booking;
import com.flightapp.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FareClient fareClient;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateBookingSuccessfully() {
        BookingRequest request = new BookingRequest();
        request.setFlightNumber("AI101");
        request.setFlightDate("2025-06-15");
        request.setPassengerName("Alice");
        request.setNumberOfSeats(2);

        FareDTO fareDTO = new FareDTO();
        fareDTO.setFare(4000.0);

        when(fareClient.getFare("AI101", "2025-06-15")).thenReturn(fareDTO);

        BookingResponse response = bookingService.createBooking(request);

        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getBookingReference());
        assertEquals(8000.0, response.getTotalFare());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void shouldFailBookingWhenFareIsNull() {
        BookingRequest request = new BookingRequest();
        request.setFlightNumber("AI101");
        request.setFlightDate("2025-06-15");
        request.setNumberOfSeats(1);

        when(fareClient.getFare(anyString(), anyString())).thenReturn(null);

        BookingResponse response = bookingService.createBooking(request);

        assertEquals("FAILED", response.getStatus());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldFailBookingWhenFareIsMissing() {
        BookingRequest request = new BookingRequest();
        request.setFlightNumber("AI101");
        request.setFlightDate("2025-06-15");
        request.setNumberOfSeats(1);

        FareDTO fareDTO = new FareDTO(); // null fare
        when(fareClient.getFare(anyString(), anyString())).thenReturn(fareDTO);

        BookingResponse response = bookingService.createBooking(request);

        assertEquals("FAILED", response.getStatus());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldReturnBookingByReference() {
        Booking booking = new Booking();
        booking.setBookingReference("REF123");

        when(bookingRepository.findByBookingReference("REF123")).thenReturn(booking);

        Booking result = bookingService.getBookingByReference("REF123");

        assertNotNull(result);
        assertEquals("REF123", result.getBookingReference());
    }

    @Test
    void shouldUpdateCheckinStatus() {
        Booking booking = new Booking();
        booking.setBookingReference("REF123");

        when(bookingRepository.findByBookingReference("REF123")).thenReturn(booking);

        BookingCheckinUpdateRequest request = new BookingCheckinUpdateRequest();
        request.setBookingReference("REF123");
        request.setCheckinNumber("CHK789");

        bookingService.updateCheckinStatus(request);

        assertTrue(booking.isCheckedIn());
        assertEquals("CHK789", booking.getCheckinNumber());
        verify(bookingRepository).save(booking);
    }

    @Test
    void shouldThrowExceptionIfBookingNotFound() {
        when(bookingRepository.findByBookingReference("INVALID")).thenReturn(null);

        BookingCheckinUpdateRequest request = new BookingCheckinUpdateRequest();
        request.setBookingReference("INVALID");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.updateCheckinStatus(request));

        assertTrue(exception.getMessage().contains("Booking not found"));
    }
    
    //new
    @Test
    void shouldReturnNullWhenBookingNotFound() {
        when(bookingRepository.findByBookingReference("NON_EXISTENT_REF")).thenReturn(null);

        Booking result = bookingService.getBookingByReference("NON_EXISTENT_REF");

        assertNull(result);
    }

}