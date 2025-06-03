package com.flightapp.search.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {

    @Test
    public void testNoArgsConstructorAndSettersAndGetters() {
        Flight flight = new Flight();

        flight.setId(1L);
        flight.setOrigin("NYC");
        flight.setDestination("LAX");
        flight.setFlightNumber("FL123");
        flight.setFlightDate("2025-06-03");
        flight.setSeatsAvailable(100);
        flight.setFare(250.5);

        assertEquals(1L, flight.getId());
        assertEquals("NYC", flight.getOrigin());
        assertEquals("LAX", flight.getDestination());
        assertEquals("FL123", flight.getFlightNumber());
        assertEquals("2025-06-03", flight.getFlightDate());
        assertEquals(100, flight.getSeatsAvailable());
        assertEquals(250.5, flight.getFare(), 0.001);
    }

    @Test
    public void testAllArgsConstructorAndGetters() {
        Flight flight = new Flight(2L, "SFO", "MIA", "FL456", "2025-07-01", 50, 180.75);

        assertEquals(2L, flight.getId());
        assertEquals("SFO", flight.getOrigin());
        assertEquals("MIA", flight.getDestination());
        assertEquals("FL456", flight.getFlightNumber());
        assertEquals("2025-07-01", flight.getFlightDate());
        assertEquals(50, flight.getSeatsAvailable());
        assertEquals(180.75, flight.getFare(), 0.001);
    }

    @Test
    public void testToString() {
        Flight flight = new Flight(3L, "BOS", "ORD", "FL789", "2025-08-15", 75, 300.0);
        String toString = flight.toString();

        assertTrue(toString.contains("id=3"));
        assertTrue(toString.contains("origin=BOS"));
        assertTrue(toString.contains("destination=ORD"));
        assertTrue(toString.contains("flightNumber=FL789"));
        assertTrue(toString.contains("flightDate=2025-08-15"));
        assertTrue(toString.contains("seatsAvailable=75"));
        assertTrue(toString.contains("fare=300.0"));
    }
}
