package com.flightapp.search.model;

import jakarta.persistence.*;

@Entity
public class Flight {

    @Id
    private String flightNumber; // primary key now

    private String origin;
    private String destination;
    private String flightDate;
    private int seatsAvailable;
    private double fare;
    private String timings;

    public Flight() {}

    public Flight(String flightNumber, String origin, String destination, String flightDate, int seatsAvailable, double fare, String timings) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.flightDate = flightDate;
        this.seatsAvailable = seatsAvailable;
        this.fare = fare;
        this.timings = timings;
    }

    // Getters and Setters

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getFlightDate() { return flightDate; }
    public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

    public int getSeatsAvailable() { return seatsAvailable; }
    public void setSeatsAvailable(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public String getTimings() { return timings; }
    public void setTimings(String timings) { this.timings = timings; }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", flightDate='" + flightDate + '\'' +
                ", seatsAvailable=" + seatsAvailable +
                ", fare=" + fare +
                ", timings='" + timings + '\'' +
                '}';
    }
}
