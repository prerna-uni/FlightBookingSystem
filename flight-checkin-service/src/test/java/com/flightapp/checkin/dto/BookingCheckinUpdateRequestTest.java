package com.flightapp.checkin.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookingCheckinUpdateRequestTest {

    @Test
    void testNoArgsConstructor() {
        // This implicitly calls the no-arg constructor
        BookingCheckinUpdateRequest dto = new BookingCheckinUpdateRequest();
        assertNotNull(dto);
        // Default values for fields should be null or false for Boolean
        assertNull(dto.getBookingReference());
        assertNull(dto.getPassengerName());
        assertNull(dto.getCheckedIn()); // Boolean defaults to null
    }

    @Test
    void testGettersAndSetters() {
        BookingCheckinUpdateRequest dto = new BookingCheckinUpdateRequest();

        // Sample data
        String bookingReference = "TESTREF123";
        String passengerName = "Alice Doe";
        Boolean checkedIn = true;

        // Set values using setters
        dto.setBookingReference(bookingReference);
        dto.setPassengerName(passengerName);
        dto.setCheckedIn(checkedIn);

        // Verify values using getters
        assertEquals(bookingReference, dto.getBookingReference());
        assertEquals(passengerName, dto.getPassengerName());
        assertEquals(checkedIn, dto.getCheckedIn());
    }

    @Test
    void testToStringMethod() {
        BookingCheckinUpdateRequest dto = new BookingCheckinUpdateRequest();
        dto.setBookingReference("BR456");
        dto.setPassengerName("Bob Smith");
        dto.setCheckedIn(false);

        String expectedToString = "BookingCheckinUpdateRequest [bookingReference=BR456, passengerName=Bob Smith, checkedIn=false]";

        assertEquals(expectedToString, dto.toString());
    }
}