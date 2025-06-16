package com.flightapp.fare.controller;

import com.flightapp.fare.dto.FareDTO;
import com.flightapp.fare.model.Fare;
import com.flightapp.fare.repository.FareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareControllerTest {

    @Mock
    private FareRepository fareRepository;

    @InjectMocks
    private FareController fareController;

    private Fare testFare;
    private String flightNumber;
    private String flightDate;
    private Double fareAmount;

    @BeforeEach
    void setUp() {
        flightNumber = "XYZ789";
        flightDate = "2025-08-15";
        fareAmount = 250.00;
        testFare = new Fare(flightNumber, flightDate, fareAmount);
        testFare.setId(1L); // Assuming Fare model has setId for consistency with previous example
    }

    @Test
    void testGetFare_Success() {
        // Mock the repository call to return a Fare object
        when(fareRepository.findByFlightNumberAndFlightDate(eq(flightNumber), eq(flightDate)))
                .thenReturn(testFare);

        // Call the controller method
        FareDTO resultFareDTO = fareController.getFare(flightNumber, flightDate);

        // Assertions
        assertNotNull(resultFareDTO);
        assertEquals(fareAmount, resultFareDTO.getFare());

        // Verify that the repository method was called exactly once with the correct arguments
        verify(fareRepository, times(1)).findByFlightNumberAndFlightDate(eq(flightNumber), eq(flightDate));
    }

    @Test
    void testGetFare_NotFound() {
        // Mock the repository call to return null (fare not found)
        when(fareRepository.findByFlightNumberAndFlightDate(any(String.class), any(String.class)))
                .thenReturn(null);

        // Assert that calling the controller method throws a ResponseStatusException
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            fareController.getFare("NONEXISTENT", "2025-01-01");
        });

        // Verify the status and reason of the exception
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Fare not found", exception.getReason());

        // Verify that the repository method was called
        verify(fareRepository, times(1)).findByFlightNumberAndFlightDate(any(String.class), any(String.class));
    }

    @Test
    void testSaveFare_Success() {
        // Create a new Fare object to be saved
        Fare newFare = new Fare("ABC101", "2025-09-20", 300.00);
        // Mock the repository's save method to return the saved fare (with an ID, as it would in a real scenario)
        when(fareRepository.save(any(Fare.class))).thenReturn(newFare);

        // Call the controller method
        Fare savedFare = fareController.saveFare(newFare);

        // Assertions
        assertNotNull(savedFare);
        assertEquals(newFare.getFlightNumber(), savedFare.getFlightNumber());
        assertEquals(newFare.getFlightDate(), savedFare.getFlightDate());
        assertEquals(newFare.getFare(), savedFare.getFare());

        // Verify that the repository's save method was called exactly once with the newFare object
        verify(fareRepository, times(1)).save(eq(newFare));
    }
}
