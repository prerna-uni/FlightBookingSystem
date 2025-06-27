package com.flightapp.booking.dto;

import java.util.List;

public class BookingRequest {
private String flightNumber;
private String flightDate;
private List<PassengerRequest> passengers;



public String getFlightNumber() { return flightNumber; }
public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

public String getFlightDate() { return flightDate; }
public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

public List<PassengerRequest> getPassengers() { return passengers; }
public void setPassengers(List<PassengerRequest> passengers) { this.passengers = passengers; }
}
