package com.flightapp.checkin.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckinRequestTest {

    @Test
    void testNoArgsConstructor() {
        CheckinRequest dto = new CheckinRequest();
        assertNotNull(dto);
        assertNull(dto.getBookingId());
        assertNull(dto.getSeatNumber());
    }

    @Test
    void testGettersAndSetters() {
        CheckinRequest dto = new CheckinRequest();
        Long bookingId = 2001L;
        String seatNumber = "12A";

        dto.setBookingId(bookingId);
        dto.setSeatNumber(seatNumber);

        assertEquals(bookingId, dto.getBookingId());
        assertEquals(seatNumber, dto.getSeatNumber());
    }

    @Test
    void testToStringMethod() {
        CheckinRequest dto = new CheckinRequest();
        dto.setBookingId(3001L);
        dto.setSeatNumber("05B");

        String expectedToString = "CheckinRequest [bookingId=3001, seatNumber=05B]";
        assertEquals(expectedToString, dto.toString());
    }
}