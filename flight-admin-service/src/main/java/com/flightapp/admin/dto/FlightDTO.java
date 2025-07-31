package com.flightapp.admin.dto;

public class FlightDTO {
private String origin;
private String destination;
private String flightNumber;
private String flightDate;
private int seatsAvailable;
private double fare;
private String timings;

public FlightDTO() {}

public FlightDTO(String origin, String destination, String flightNumber, String flightDate, int seatsAvailable, double fare,String timings) {
    this.origin = origin;
    this.destination = destination;
    this.flightNumber = flightNumber;
    this.flightDate = flightDate;
    this.seatsAvailable = seatsAvailable;
    this.fare = fare;
    this.timings=timings;
}

public String getOrigin() { return origin; }
public void setOrigin(String origin) { this.origin = origin; }

public String getDestination() { return destination; }
public void setDestination(String destination) { this.destination = destination; }

public String getFlightNumber() { return flightNumber; }
public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

public String getFlightDate() { return flightDate; }
public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

public int getSeatsAvailable() { return seatsAvailable; }
public void setSeatsAvailable(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }

public double getFare() { return fare; }
public void setFare(double fare) { this.fare = fare; }

public String getTimings() {
	return timings;
}

public void setTimings(String timings) {
	this.timings = timings;
}


}