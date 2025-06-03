package com.flightapp.booking.dto;

public class BookingDTO {
    private String bookingReference;
    private String checkinNumber;

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getCheckinNumber() {
        return checkinNumber;
    }

    public void setCheckinNumber(String checkinNumber) {
        this.checkinNumber = checkinNumber;
    }
}
