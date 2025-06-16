package com.flightapp.fare.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareDTOTest {

    private Double testFareValue;

    @BeforeEach
    void setUp() {
        // Initialize common test data before each test
        testFareValue = 199.99;
    }

    @Test
    void testNoArgsConstructor() {
        // Test the no-argument constructor
        FareDTO fareDTO = new FareDTO();
        assertNotNull(fareDTO);
        assertNull(fareDTO.getFare()); // Fare should be null by default
    }

    @Test
    void testGettersAndSetters() {
        FareDTO fareDTO = new FareDTO();

        // Test setFare and getFare
        fareDTO.setFare(testFareValue);
        assertEquals(testFareValue, fareDTO.getFare());

        // Test with a different value to ensure it updates correctly
        Double newFareValue = 250.50;
        fareDTO.setFare(newFareValue);
        assertEquals(newFareValue, fareDTO.getFare());
    }

    @Test
    void testToString() {
        FareDTO fareDTO = new FareDTO();
        fareDTO.setFare(testFareValue);

        String expectedToString = "FareDTO{" +
                                  "fare=" + testFareValue +
                                  '}';
        assertEquals(expectedToString, fareDTO.toString());
    }

    @Test
    void testToStringWithNullFare() {
        // Test toString when fare is null
        FareDTO fareDTO = new FareDTO(); // fare will be null by default

        String expectedToString = "FareDTO{" +
                                  "fare=" + null +
                                  '}';
        assertEquals(expectedToString, fareDTO.toString());
    }
}