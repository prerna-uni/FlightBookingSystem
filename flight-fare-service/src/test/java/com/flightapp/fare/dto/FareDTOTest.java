package com.flightapp.fare.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareDTOTest {

    @Test
    void testGetterSetterAndToString() {
        FareDTO fareDTO = new FareDTO();

        // Test setter
        fareDTO.setFare(150.75);

        // Test getter
        assertEquals(150.75, fareDTO.getFare());

        // Test toString contains fare value
        String toStringResult = fareDTO.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("150.75"));
        assertTrue(toStringResult.contains("FareDTO"));
    }

    // Explicitly test default constructor state
    @Test
    void testDefaultConstructor() {
        FareDTO dto = new FareDTO();
        assertNull(dto.getFare());
    }
}
