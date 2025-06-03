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
}