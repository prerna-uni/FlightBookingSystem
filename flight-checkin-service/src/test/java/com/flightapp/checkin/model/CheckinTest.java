package com.flightapp.checkin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckinTest {

    @Test
    public void testGettersAndSetters() {
        Checkin checkin = new Checkin();

        checkin.setId(100L);
        checkin.setBookingId(200L);
        checkin.setCheckedIn(true);

        assertEquals(100L, checkin.getId());
        assertEquals(200L, checkin.getBookingId());
        assertTrue(checkin.getCheckedIn());
    }

    @Test
    public void testToString() {
        Checkin checkin = new Checkin();
        checkin.setId(1L);
        checkin.setBookingId(10L);
        checkin.setCheckedIn(false);

        String expected = "Checkin{id=1, bookingId=10, checkedIn=false}";
        assertEquals(expected, checkin.toString());
    }

    @Test
    public void testDefaultConstructor() {
        Checkin checkin = new Checkin();
        assertNull(checkin.getId());
        assertNull(checkin.getBookingId());
        assertNull(checkin.getCheckedIn());
    }
    
    //new
    @Test
    public void testSetCheckedInToFalse() {
        Checkin checkin = new Checkin();
        checkin.setCheckedIn(false);
        assertFalse(checkin.getCheckedIn());
    }

    @Test
    public void testSetNullValuesExplicitly() {
        Checkin checkin = new Checkin();
        checkin.setId(null);
        checkin.setBookingId(null);
        checkin.setCheckedIn(null);

        assertNull(checkin.getId());
        assertNull(checkin.getBookingId());
        assertNull(checkin.getCheckedIn());
    }
    
    @Test
    public void testToStringConsistencyAfterUpdates() {
        Checkin checkin = new Checkin();
        checkin.setId(5L);
        checkin.setBookingId(50L);
        checkin.setCheckedIn(true);

        String expected = "Checkin{id=5, bookingId=50, checkedIn=true}";
        assertEquals(expected, checkin.toString());

        // Update values and verify again
        checkin.setId(10L);
        checkin.setBookingId(100L);
        checkin.setCheckedIn(false);

        expected = "Checkin{id=10, bookingId=100, checkedIn=false}";
        assertEquals(expected, checkin.toString());
    }

}