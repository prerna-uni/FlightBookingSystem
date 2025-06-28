package com.flightapp.search.service;

import com.flightapp.search.dto.FlightResponse;
import com.flightapp.search.model.Flight;
import com.flightapp.search.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<FlightResponse> searchFlights(String origin, String destination, String date) {
        List<Flight> flights = flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date);
        return flights.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<FlightResponse> getAllFlightResponses() {
        return flightRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<Flight> getAllFlightsRaw() {
        return flightRepository.findAll();
    }

    private FlightResponse mapToResponse(Flight flight) {
        FlightResponse response = new FlightResponse();
        response.setFlightNumber(flight.getFlightNumber());
        response.setOrigin(flight.getOrigin());
        response.setDestination(flight.getDestination());
        response.setFlightDate(flight.getFlightDate());
        response.setSeatsAvailable(flight.getSeatsAvailable());
        response.setFare(flight.getFare());
        response.setTimings(flight.getTimings());
        return response;
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight updateFlight(String flightNumber, Flight updatedFlight) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightNumber);
        if (optionalFlight.isPresent()) {
            Flight existingFlight = optionalFlight.get();
            existingFlight.setOrigin(updatedFlight.getOrigin());
            existingFlight.setDestination(updatedFlight.getDestination());
            existingFlight.setFlightDate(updatedFlight.getFlightDate());
            existingFlight.setSeatsAvailable(updatedFlight.getSeatsAvailable());
            existingFlight.setFare(updatedFlight.getFare());
            existingFlight.setTimings(updatedFlight.getTimings());
            return flightRepository.save(existingFlight);
        } else {
            throw new RuntimeException("Flight with flightNumber " + flightNumber + " not found.");
        }
    }

    public void deleteFlight(String flightNumber) {
        flightRepository.deleteById(flightNumber);
    }
}
