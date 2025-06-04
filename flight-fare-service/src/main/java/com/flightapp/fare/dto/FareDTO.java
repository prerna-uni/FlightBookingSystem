package com.flightapp.fare.dto;

public class FareDTO {
    private Double fare;

    public FareDTO() {}

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }
    
    @Override
    public String toString() {
        return "FareDTO{" +
               "fare=" + fare +
               '}';
    }

}
