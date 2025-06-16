package com.flightapp.admin.dto;

public class FareDTO {
private String flightNumber;
private String flightDate;
private double fare;

public FareDTO() {}

public FareDTO(String flightNumber, String flightDate, double fare) {
    this.flightNumber = flightNumber;
    this.flightDate = flightDate;
    this.fare = fare;
}

public String getFlightNumber() { return flightNumber; }
public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

public String getFlightDate() { return flightDate; }
public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

public double getFare() { return fare; }
public void setFare(double fare) { this.fare = fare; }
}