package com.flightapp.checkin.service;

import com.flightapp.checkin.client.BookingClient;
import com.flightapp.checkin.client.PaymentClient;
import com.flightapp.checkin.dto.BookingCheckinUpdateRequest;
import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.model.Checkin;
import com.flightapp.checkin.repository.CheckinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull; // Import assertNotNull
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckinServiceTest {

    @Mock
    private CheckinRepository checkinRepository;

    @Mock
    private BookingClient bookingClient;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private CheckinService checkinService;

    private BookingDTO bookingDTO;
    private String bookingReference;
    private Long bookingId;
    private String passengerName;

    @BeforeEach
    void setUp() {
        bookingId = 123L;
        bookingReference = "REF456";
        passengerName = "Alice Wonderland";

        bookingDTO = new BookingDTO();
        bookingDTO.setId(bookingId);
        bookingDTO.setBookingReference(bookingReference);
        bookingDTO.setFlightNumber("FL202");
        bookingDTO.setFlightDate("2025-08-01");
        bookingDTO.setPassengerName(passengerName);
        bookingDTO.setCheckedIn(false); // Initial state
    }

    // --- New test to explicitly cover the constructor ---
    @Test
    void testServiceConstructorInitialization() {
        // Manually create an instance to ensure the constructor is called directly
        // and its lines (field assignments) are covered.
        CheckinService service = new CheckinService(checkinRepository, bookingClient, paymentClient);
        assertNotNull(service, "Service should be initialized");
        // No further assertions are typically made as private fields aren't directly accessible,
        // but this ensures the constructor's lines are executed.
    }

    // --- Existing Test cases for performCheckin method ---

    @Test
    void testPerformCheckin_Success() {
        // Given
        when(paymentClient.isPaymentCompleted(bookingReference)).thenReturn(true);
        when(checkinRepository.findByBookingId(bookingId)).thenReturn(null);
        when(checkinRepository.save(any(Checkin.class))).thenReturn(new Checkin());
        doNothing().when(bookingClient).updateCheckinStatus(any(BookingCheckinUpdateRequest.class));

        // When
        String result = checkinService.performCheckin(bookingDTO);

        // Then
        assertEquals("Check-in successful for booking ID " + bookingId, result);

        // Verify interactions
        verify(paymentClient, times(1)).isPaymentCompleted(bookingReference);
        verify(checkinRepository, times(1)).findByBookingId(bookingId);
        verify(checkinRepository, times(1)).save(any(Checkin.class));
        verify(bookingClient, times(1)).updateCheckinStatus(any(BookingCheckinUpdateRequest.class));
    }

    @Test
    void testPerformCheckin_PaymentNotCompleted_ReturnsMessage() {
        // Given
        when(paymentClient.isPaymentCompleted(bookingReference)).thenReturn(false);

        // When
        String result = checkinService.performCheckin(bookingDTO);

        // Then
        assertEquals("Payment not completed for booking reference " + bookingReference + ". Check-in not allowed.", result);

        // Verify interactions: Only payment client was called
        verify(paymentClient, times(1)).isPaymentCompleted(bookingReference);
        verifyNoInteractions(checkinRepository);
        verifyNoInteractions(bookingClient);
    }

    @Test
    void testPerformCheckin_PaymentClientReturnsNull_ReturnsMessage() {
        // Given
        when(paymentClient.isPaymentCompleted(bookingReference)).thenReturn(null);

        // When
        String result = checkinService.performCheckin(bookingDTO);

        // Then
        assertEquals("Payment not completed for booking reference " + bookingReference + ". Check-in not allowed.", result);

        // Verify interactions: Only payment client was called
        verify(paymentClient, times(1)).isPaymentCompleted(bookingReference);
        verifyNoInteractions(checkinRepository);
        verifyNoInteractions(bookingClient);
    }

    @Test
    void testPerformCheckin_AlreadyCheckedIn_ReturnsMessage() {
        // Given
        when(paymentClient.isPaymentCompleted(bookingReference)).thenReturn(true);
        Checkin existingCheckin = new Checkin();
        existingCheckin.setBookingId(bookingId);
        existingCheckin.setCheckedIn(true);
        when(checkinRepository.findByBookingId(bookingId)).thenReturn(existingCheckin);

        // When
        String result = checkinService.performCheckin(bookingDTO);

        // Then
        assertEquals("Already checked in", result);

        // Verify interactions:
        verify(paymentClient, times(1)).isPaymentCompleted(bookingReference);
        verify(checkinRepository, times(1)).findByBookingId(bookingId);
        verify(checkinRepository, never()).save(any(Checkin.class));
        verifyNoInteractions(bookingClient);
    }

    @Test
    void testPerformCheckin_ExistingCheckinFalse_Success() {
        // Given
        when(paymentClient.isPaymentCompleted(bookingReference)).thenReturn(true);

        Checkin existingCheckinInDB = new Checkin();
        existingCheckinInDB.setBookingId(bookingId);
        existingCheckinInDB.setCheckedIn(false);
        when(checkinRepository.findByBookingId(bookingId)).thenReturn(existingCheckinInDB);

        ArgumentCaptor<Checkin> checkinCaptor = ArgumentCaptor.forClass(Checkin.class);

        when(checkinRepository.save(checkinCaptor.capture())).thenReturn(existingCheckinInDB);
        doNothing().when(bookingClient).updateCheckinStatus(any(BookingCheckinUpdateRequest.class));

        // When
        String result = checkinService.performCheckin(bookingDTO);

        // Then
        assertEquals("Check-in successful for booking ID " + bookingId, result);

        verify(paymentClient, times(1)).isPaymentCompleted(bookingReference);
        verify(checkinRepository, times(1)).findByBookingId(bookingId);
        verify(checkinRepository, times(1)).save(any(Checkin.class));
        verify(bookingClient, times(1)).updateCheckinStatus(any(BookingCheckinUpdateRequest.class));

        Checkin capturedCheckin = checkinCaptor.getValue();
        assertEquals(true, capturedCheckin.getCheckedIn(), "The checkedIn status of the saved Checkin should be true.");
        assertEquals(bookingId, capturedCheckin.getBookingId());
    }
}