package com.flightapp.fare.controller;

import com.flightapp.fare.dto.FareDTO;
import com.flightapp.fare.model.Fare;
import com.flightapp.fare.repository.FareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FareControllerTest {

    @Mock
    private FareRepository fareRepository;

    @InjectMocks
    private FareController fareController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDefaultConstructor() {
        FareController controller = new FareController();
        assertNotNull(controller);
    }

    @Test
    void testGetFare_Success() {
        Fare mockFare = new Fare("AI101", "2025-06-02", 2500.0);
        when(fareRepository.findByFlightNumberAndFlightDate("AI101", "2025-06-02"))
                .thenReturn(mockFare);

        FareDTO fareDTO = fareController.getFare("AI101", "2025-06-02");

        assertNotNull(fareDTO);
        assertEquals(2500.0, fareDTO.getFare());
        verify(fareRepository, times(1)).findByFlightNumberAndFlightDate("AI101", "2025-06-02");
    }

    @Test
    void testGetFare_NotFound() {
        when(fareRepository.findByFlightNumberAndFlightDate("XX123", "2025-06-02"))
                .thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                fareController.getFare("XX123", "2025-06-02"));

        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Fare not found"));
    }

    @Test
    void testGetFare_EmptyStrings() {
        when(fareRepository.findByFlightNumberAndFlightDate("", ""))
            .thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            fareController.getFare("", "");
        });

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void testSaveFare_Success() {
        Fare inputFare = new Fare("BA202", "2025-06-03", 3200.0);
        inputFare.setFlightNumber("BA202");
        inputFare.setFlightDate("2025-06-03");
        inputFare.setFare(3200.0);

        Fare savedFare = new Fare("BA202", "2025-06-03", 3200.0);

        when(fareRepository.save(inputFare)).thenReturn(savedFare);

        Fare result = fareController.saveFare(inputFare);

        assertNotNull(result);
        assertEquals("BA202", result.getFlightNumber());
        assertEquals("2025-06-03", result.getFlightDate());
        assertEquals(3200.0, result.getFare());
        assertTrue(result.toString().contains("BA202"));

        verify(fareRepository, times(1)).save(inputFare);
    }

    @Test
    void testSaveFare_NullFields() {
        Fare fare = new Fare();
        fare.setFlightNumber(null);
        fare.setFlightDate(null);
        fare.setFare(null);

        when(fareRepository.save(fare)).thenReturn(fare);

        Fare result = fareController.saveFare(fare);

        assertNull(result.getFlightNumber());
        assertNull(result.getFlightDate());
        assertNull(result.getFare());
        assertNotNull(result.toString());

        verify(fareRepository, times(1)).save(fare);
    }

    @Test
    void testSaveFare_PartialNullFields() {
        Fare fare = new Fare();
        fare.setFlightNumber("AI999");
        fare.setFlightDate(null);
        fare.setFare(1999.99);

        when(fareRepository.save(fare)).thenReturn(fare);

        Fare result = fareController.saveFare(fare);

        assertNotNull(result);
        assertEquals("AI999", result.getFlightNumber());
        assertNull(result.getFlightDate());
        assertEquals(1999.99, result.getFare());

        verify(fareRepository, times(1)).save(fare);
    }

    @Test
    void testGetFare_RepositoryThrowsException() {
        when(fareRepository.findByFlightNumberAndFlightDate(anyString(), anyString()))
            .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            fareController.getFare("ANY", "2025-01-01");
        });

        assertEquals("DB error", ex.getMessage());
    }
}
