package com.flightapp.booking.dto;
public class BookingResponse {
    private String bookingReference;
    private String status;
    private String message;
    private String checkinStatus;  
    private Double totalFare;
    
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getCheckinStatus() { return checkinStatus; }
    public void setCheckinStatus(String checkinStatus) { this.checkinStatus = checkinStatus; }
    
    public Double getTotalFare() { return totalFare; }
    public void setTotalFare(Double totalFare) { this.totalFare = totalFare; }
    
    @Override
    public String toString() {
        return "BookingResponse{" +
                "bookingReference='" + bookingReference + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", checkinStatus='" + checkinStatus + '\'' +
                ", totalFare=" + totalFare +
                '}';
    
    }

    
}
