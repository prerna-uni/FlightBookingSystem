package com.flightapp.checkin.controller;

import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.service.CheckinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckinControllerTest {

    @Mock
    private CheckinService checkinService;

    @InjectMocks
    private CheckinController checkinController;

    private BookingDTO bookingDTO;
    private String expectedMessage;

    @BeforeEach
    void setUp() {
        // Initialize a sample BookingDTO for testing
        bookingDTO = new BookingDTO();
        bookingDTO.setId(123L);
        bookingDTO.setBookingReference("REF456");
        bookingDTO.setFlightNumber("FL202");
        bookingDTO.setFlightDate("2025-08-01");
        bookingDTO.setPassengerName("Alice Wonderland");
        bookingDTO.setCheckedIn(false); // Initial state

        expectedMessage = "Check-in confirmed for Alice Wonderland!";
    }

    @Test
    void testConfirmCheckin_Success() {
        // Given
        // Mock the performCheckin method of the CheckinService
        when(checkinService.performCheckin(any(BookingDTO.class))).thenReturn(expectedMessage);

        // When
        // Call the controller's confirmCheckin method with the sample BookingDTO
        ResponseEntity<String> responseEntity = checkinController.confirmCheckin(bookingDTO);

        // Then
        // Assert that the response entity is not null
        assertNotNull(responseEntity);
        // Assert that the HTTP status code is OK (200)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Assert that the response body matches the expected message from the service
        assertEquals(expectedMessage, responseEntity.getBody());

        // Verify that the performCheckin method on the mocked service was called exactly once
        // with the specific bookingDTO (or any BookingDTO if you prefer a more general check)
        verify(checkinService, times(1)).performCheckin(bookingDTO);
    }
}