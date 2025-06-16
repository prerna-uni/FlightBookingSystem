package com.flightapp.fare.controller;

import com.flightapp.fare.model.Fare;
import com.flightapp.fare.repository.FareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminFareControllerTest {

    @Mock
    private FareRepository fareRepository;

    @InjectMocks
    private AdminFareController adminFareController;

    private Fare testFare;

    @BeforeEach
    void setUp() {
        testFare = new Fare("FN123", "2025-07-01", 150.00);
        testFare.setId(1L); // Set an ID for the test fare
    }

    @Test
    void testAddFare_Success() {
        when(fareRepository.save(any(Fare.class))).thenReturn(testFare);

        ResponseEntity<?> responseEntity = adminFareController.addFare(testFare);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(testFare, responseEntity.getBody());
        verify(fareRepository, times(1)).save(any(Fare.class));
    }

    @Test
    void testUpdateFare_Success() {
        Fare updatedFareDetails = new Fare("FN123", "2025-07-02", 175.00);
        updatedFareDetails.setId(1L);

        when(fareRepository.findById(1L)).thenReturn(Optional.of(testFare));
        when(fareRepository.save(any(Fare.class))).thenReturn(updatedFareDetails);

        ResponseEntity<?> responseEntity = adminFareController.updateFare(1L, updatedFareDetails);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        Fare returnedFare = (Fare) responseEntity.getBody();
        assertEquals(updatedFareDetails.getFlightNumber(), returnedFare.getFlightNumber());
        assertEquals(updatedFareDetails.getFlightDate(), returnedFare.getFlightDate());
        assertEquals(updatedFareDetails.getFare(), returnedFare.getFare());
        verify(fareRepository, times(1)).findById(1L);
        verify(fareRepository, times(1)).save(any(Fare.class));
    }

    @Test
    void testUpdateFare_NotFound() {
        Fare updatedFareDetails = new Fare("FN123", "2025-07-02", 175.00);
        updatedFareDetails.setId(99L);

        when(fareRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = adminFareController.updateFare(99L, updatedFareDetails);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Fare not found with id: 99", responseEntity.getBody());
        verify(fareRepository, times(1)).findById(99L);
        verify(fareRepository, never()).save(any(Fare.class)); // Ensure save is not called
    }

    @Test
    void testDeleteFare_Success() {
        when(fareRepository.existsById(1L)).thenReturn(true);
        doNothing().when(fareRepository).deleteById(1L);

        ResponseEntity<?> responseEntity = adminFareController.deleteFare(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Fare deleted successfully", responseEntity.getBody());
        verify(fareRepository, times(1)).existsById(1L);
        verify(fareRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteFare_NotFound() {
        when(fareRepository.existsById(99L)).thenReturn(false);

        ResponseEntity<?> responseEntity = adminFareController.deleteFare(99L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Fare not found with id: 99", responseEntity.getBody());
        verify(fareRepository, times(1)).existsById(99L);
        verify(fareRepository, never()).deleteById(anyLong());
    }
}