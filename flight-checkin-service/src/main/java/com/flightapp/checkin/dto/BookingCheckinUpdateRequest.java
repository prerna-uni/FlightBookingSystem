package com.flightapp.checkin.dto;

public class BookingCheckinUpdateRequest {
    private String bookingReference;
    private String passengerName;
    private Boolean checkedIn;

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

	@Override
	public String toString() {
		return "BookingCheckinUpdateRequest [bookingReference=" + bookingReference + ", passengerName=" + passengerName
				+ ", checkedIn=" + checkedIn + "]";
	}
    
}
