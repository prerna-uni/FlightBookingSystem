package com.flightapp.payment.model;

import jakarta.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingReference;
    private Double amount;
    private String paymentId;
    private boolean status;

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getBookingReference() { return bookingReference; }

    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }

    public String getPaymentId() { return paymentId; }

    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public boolean isStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }
}
