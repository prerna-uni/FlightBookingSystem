package com.flightapp.checkin.dto;


public class BookingDTO {
    private Long id;
    private String bookingReference;
    private String flightNumber;
    private String flightDate;
    private String passengerName;
    private Boolean checkedIn;

    public BookingDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getFlightDate() { return flightDate; }
    public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public Boolean getCheckedIn() { return checkedIn; }
    public void setCheckedIn(Boolean checkedIn) { this.checkedIn = checkedIn; }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "id=" + id +
                ", bookingReference='" + bookingReference + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", flightDate='" + flightDate + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", checkedIn=" + checkedIn +
                '}';
    }
}
