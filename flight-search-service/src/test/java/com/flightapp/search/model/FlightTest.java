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
    
    //new
    @Test
    public void testDefaultValuesAfterNoArgsConstructor() {
        Flight flight = new Flight();

        assertNull(flight.getId());
        assertNull(flight.getOrigin());
        assertNull(flight.getDestination());
        assertNull(flight.getFlightNumber());
        assertNull(flight.getFlightDate());
        assertEquals(0, flight.getSeatsAvailable());
        assertEquals(0.0, flight.getFare(), 0.001);
    }
    @Test
    public void testFlightObjectInequality() {
        Flight flight1 = new Flight(1L, "DEL", "BOM", "AI202", "2025-09-10", 20, 400.0);
        Flight flight2 = new Flight(2L, "DEL", "BLR", "AI203", "2025-09-11", 30, 500.0);

        assertNotEquals(flight1.getId(), flight2.getId());
        assertNotEquals(flight1.getDestination(), flight2.getDestination());
        assertNotEquals(flight1.getFare(), flight2.getFare(), 0.001);
    }
    

}
