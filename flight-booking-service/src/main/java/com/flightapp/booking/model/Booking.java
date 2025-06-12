package com.flightapp.booking.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Booking {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String flightNumber;
private String flightDate;
private String bookingReference;
private boolean checkedIn;
private String checkinNumber;
private Double totalFare;
private boolean paymentDone;

@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
@JsonManagedReference
private List<Passenger> passengers;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getFlightNumber() {
	return flightNumber;
}

public void setFlightNumber(String flightNumber) {
	this.flightNumber = flightNumber;
}

public String getFlightDate() {
	return flightDate;
}

public void setFlightDate(String flightDate) {
	this.flightDate = flightDate;
}

public String getBookingReference() {
	return bookingReference;
}

public void setBookingReference(String bookingReference) {
	this.bookingReference = bookingReference;
}

public boolean isCheckedIn() {
	return checkedIn;
}

public void setCheckedIn(boolean checkedIn) {
	this.checkedIn = checkedIn;
}

public String getCheckinNumber() {
	return checkinNumber;
}

public void setCheckinNumber(String checkinNumber) {
	this.checkinNumber = checkinNumber;
}

public Double getTotalFare() {
	return totalFare;
}

public void setTotalFare(Double totalFare) {
	this.totalFare = totalFare;
}

public boolean isPaymentDone() {
	return paymentDone;
}

public void setPaymentDone(boolean paymentDone) {
	this.paymentDone = paymentDone;
}

public List<Passenger> getPassengers() {
	return passengers;
}

public void setPassengers(List<Passenger> passengers) {
	this.passengers = passengers;
}

// Getters and setters ...
}
