package com.flightapp.search.dto;

public class FlightResponse {
    private Long id;
    private String origin;
    private String destination;
    private String flightNumber;
    private String flightDate;
    private int seatsAvailable;
    private Double fare;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Double getFare() { return fare; }
    public void setFare(Double fare) { this.fare = fare; }
}
