package com.flightapp.admin.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlightDTOTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        FlightDTO flightDTO = new FlightDTO();

        flightDTO.setOrigin("DEL");
        flightDTO.setDestination("BLR");
        flightDTO.setFlightNumber("IND123");
        flightDTO.setFlightDate("2025-10-10");
        flightDTO.setSeatsAvailable(180);
        flightDTO.setFare(2999.99);
        flightDTO.setTimings("6:00 AM");

        assertEquals("DEL", flightDTO.getOrigin());
        assertEquals("BLR", flightDTO.getDestination());
        assertEquals("IND123", flightDTO.getFlightNumber());
        assertEquals("2025-10-10", flightDTO.getFlightDate());
        assertEquals(180, flightDTO.getSeatsAvailable());
        assertEquals(2999.99, flightDTO.getFare());
        assertEquals("6:00 AM", flightDTO.getTimings());
    }

    @Test
    void testAllArgsConstructor() {
        FlightDTO flightDTO = new FlightDTO("DEL", "BLR", "IND123", "2025-10-10", 180, 2999.99, "6:00 AM");

        assertEquals("DEL", flightDTO.getOrigin());
        assertEquals("BLR", flightDTO.getDestination());
        assertEquals("IND123", flightDTO.getFlightNumber());
        assertEquals("2025-10-10", flightDTO.getFlightDate());
        assertEquals(180, flightDTO.getSeatsAvailable());
        assertEquals(2999.99, flightDTO.getFare());
        assertEquals("6:00 AM", flightDTO.getTimings());
    }
}