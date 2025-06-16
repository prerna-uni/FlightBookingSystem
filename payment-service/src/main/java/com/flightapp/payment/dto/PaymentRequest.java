package com.flightapp.payment.dto;

public class PaymentRequest {
    private String bookingReference;
    private Double amount;

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "bookingReference='" + bookingReference + '\'' +
                ", amount=" + amount +
                '}';
    }
}
