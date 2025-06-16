package com.flightapp.fare.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fares")

public class Fare {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String flightNumber;
	private String flightDate;
	private Double fare;

	public Fare() {}

	public Fare(String flightNumber, String flightDate, Double fare) {
	    this.flightNumber = flightNumber;
	    this.flightDate = flightDate;
	    this.fare = fare;
	}

	public Long getId() {
	    return id;
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

	public Double getFare() {
	    return fare;
	}

	public void setFare(Double fare) {
	    this.fare = fare;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
	    return "Fare{" +
	           "id=" + id +
	           ", flightNumber='" + flightNumber + '\'' +
	           ", flightDate='" + flightDate + '\'' +
	           ", fare=" + fare +
	           '}';
	}



}
