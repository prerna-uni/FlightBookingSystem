package com.flightapp.search.model;

import jakarta.persistence.*;

@Entity

public class Flight {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String origin;
	private String destination;
	private String flightNumber;
	private String flightDate;
	private int seatsAvailable;
	private double fare;
	

	public Flight() {}

	public Flight(Long id, String origin, String destination, String flightNumber, String flightDate, int seatsAvailable, double fare) {
	    this.id = id;
	    this.origin = origin;
	    this.destination = destination;
	    this.flightNumber = flightNumber;
	    this.flightDate = flightDate;
	    this.seatsAvailable = seatsAvailable;
	    this.fare=fare;
	    
	}

	// Getters and setters

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

	public double getFare() { return fare; }
	public void setFare(double fare) { this.fare = fare; }

	@Override
	public String toString() {
		return "Flight [id=" + id + ", origin=" + origin + ", destination=" + destination + ", flightNumber="
				+ flightNumber + ", flightDate=" + flightDate + ", seatsAvailable=" + seatsAvailable + ", fare=" + fare
				+ "]";
	}

	

	


}
