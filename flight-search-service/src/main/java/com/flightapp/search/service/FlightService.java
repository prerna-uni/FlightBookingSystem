package com.flightapp.search.service;

import com.flightapp.search.model.Flight;
import com.flightapp.search.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {


private final FlightRepository flightRepository;

@Autowired
public FlightService(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
}

public List<Flight> searchFlights(String origin, String destination, String date) {
    return flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date);
}
}