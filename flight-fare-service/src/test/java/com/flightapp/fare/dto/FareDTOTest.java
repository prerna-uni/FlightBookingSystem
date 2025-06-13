package com.flightapp.fare.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareDTOTest {

    @Test
    void testGetterSetterAndToString() {
        FareDTO fareDTO = new FareDTO();

       
        fareDTO.setFare(150.75);

        
        assertEquals(150.75, fareDTO.getFare());

        
        String toStringResult = fareDTO.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("150.75"));
        assertTrue(toStringResult.contains("FareDTO"));
    }

    
    @Test
    void testDefaultConstructor() {
        FareDTO dto = new FareDTO();
        assertNull(dto.getFare());
    }
    
    //new
    @Test
    void testSetterWithZero() {
        FareDTO dto = new FareDTO();
        dto.setFare(0.0);
        assertEquals(0.0, dto.getFare());
    }

    @Test
    void testSetterWithNegativeFare() {
        FareDTO dto = new FareDTO();
        dto.setFare(-100.0);
        assertEquals(-100.0, dto.getFare());
    }

    @Test
    void testSetterWithLargeFare() {
        FareDTO dto = new FareDTO();
        double largeValue = Double.MAX_VALUE;
        dto.setFare(largeValue);
        assertEquals(largeValue, dto.getFare());
    }

    @Test
    void testToStringFormat() {
        FareDTO dto = new FareDTO();
        dto.setFare(123.45);
        String toString = dto.toString();
       
        assertTrue(toString.contains("fare=123.45"));
    }
}
