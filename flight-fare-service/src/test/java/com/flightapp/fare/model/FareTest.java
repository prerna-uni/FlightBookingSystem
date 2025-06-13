package com.flightapp.fare.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareTest {

    @Test
    void testDefaultConstructorAndGettersSetters() {
        Fare fare = new Fare();

        
        assertNull(fare.getId());
        assertNull(fare.getFlightNumber());
        assertNull(fare.getFlightDate());
        assertNull(fare.getFare());

        
        fare.setFlightNumber("AI101");
        fare.setFlightDate("2025-06-02");
        fare.setFare(2500.0);

        
        assertNull(fare.getId());

        
        assertEquals("AI101", fare.getFlightNumber());
        assertEquals("2025-06-02", fare.getFlightDate());
        assertEquals(2500.0, fare.getFare());
    }

    @Test
    void testParameterizedConstructor() {
        Fare fare = new Fare("BA202", "2025-07-01", 3200.0);

        
        assertNull(fare.getId());

        assertEquals("BA202", fare.getFlightNumber());
        assertEquals("2025-07-01", fare.getFlightDate());
        assertEquals(3200.0, fare.getFare());
    }

    @Test
    void testToString() {
        Fare fare = new Fare("AI303", "2025-08-15", 2800.0);

        String toString = fare.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("AI303"));
        assertTrue(toString.contains("2025-08-15"));
        assertTrue(toString.contains("2800.0"));
        assertTrue(toString.contains("id=")); 
    }
    
    //new
    
    
    @Test
    void testSetFareWithLargeValue() {
        Fare fare = new Fare();
        double largeFare = Double.MAX_VALUE;
        fare.setFare(largeFare);
        assertEquals(largeFare, fare.getFare());
    }


}
