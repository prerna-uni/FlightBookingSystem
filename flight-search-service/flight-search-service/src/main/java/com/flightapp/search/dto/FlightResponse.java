package com.flightapp.search.dto;

public class FlightResponse {
    
    private String origin;
    private String destination;
    private String flightNumber;
    private String flightDate;
    private int seatsAvailable;
    private Double fare;
    private String timings;

   
    public FlightResponse() {
        // Required for Spring deserialization and many frameworks
    }

    // Your existing all-argument constructor
    public FlightResponse(String origin, String destination, String flightNumber, String flightDate,
                          int seatsAvailable, Double fare,String timings) {
        super(); // Calls the constructor of the superclass (Object), not strictly needed but harmless
        
        this.origin = origin;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.seatsAvailable = seatsAvailable;
        this.fare = fare;
        this.timings=timings;
    }

    // Getters
    
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getFlightNumber() { return flightNumber; }
    public String getFlightDate() { return flightDate; }
    public int getSeatsAvailable() { return seatsAvailable; }
    public Double getFare() { return fare; }

    // Setters
    
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setFlightDate(String flightDate) { this.flightDate = flightDate; }
    public void setSeatsAvailable(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }
    public void setFare(Double fare) { this.fare = fare; }
    
    

	public String getTimings() {
		return timings;
	}

	public void setTimings(String timings) {
		this.timings = timings;
	}

	@Override
	public String toString() {
	    return "Flight{" +
	           
	           ", origin='" + origin + '\'' +
	           ", destination='" + destination + '\'' +
	           ", flightNumber='" + flightNumber + '\'' +
	           ", flightDate='" + flightDate + '\'' +
	           ", seatsAvailable=" + seatsAvailable +
	           ", fare=" + fare +
	           ", timings='" + timings + '\'' +
	           '}';
	}
    
}