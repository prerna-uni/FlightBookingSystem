package com.flightapp.search.service;

import com.flightapp.search.dto.FareDTO;
import com.flightapp.search.dto.FlightResponse;
import com.flightapp.search.feign.FareClient;
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
	private final FareClient fareClient;

	@Autowired
	public FlightService(FlightRepository flightRepository, FareClient fareClient) {
	    this.flightRepository = flightRepository;
	    this.fareClient = fareClient;
	}

	// USER-facing search method
	public List<FlightResponse> searchFlights(String origin, String destination, String date) {
	    List<Flight> flights = flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date);

	    return flights.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	// NEW: ADMIN or UI view — return all flights
	public List<FlightResponse> getAllFlights() {
	    List<Flight> flights = flightRepository.findAll();
	    return flights.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	// helper method to convert Flight to FlightResponse with fare
	private FlightResponse mapToResponse(Flight flight) {
	    FareDTO fareDTO = null;
	    try {
	        fareDTO = fareClient.getFare(flight.getFlightNumber(), flight.getFlightDate());
	    } catch (Exception e) {
	        // Optionally log or handle this error
	    }

	    FlightResponse response = new FlightResponse();
	    response.setId(flight.getId());
	    response.setOrigin(flight.getOrigin());
	    response.setDestination(flight.getDestination());
	    response.setFlightNumber(flight.getFlightNumber());
	    response.setFlightDate(flight.getFlightDate());
	    response.setSeatsAvailable(flight.getSeatsAvailable());
	    response.setFare(fareDTO != null ? fareDTO.getFare() : null);

	    return response;
	}

	// ADMIN: save flight
	public Flight saveFlight(Flight flight) {
	    return flightRepository.save(flight);
	}

	// ADMIN: update flight
	public Flight updateFlight(Long id, Flight updatedFlight) {
	    Optional<Flight> optionalFlight = flightRepository.findById(id);
	    if (optionalFlight.isPresent()) {
	        Flight existingFlight = optionalFlight.get();
	        existingFlight.setOrigin(updatedFlight.getOrigin());
	        existingFlight.setDestination(updatedFlight.getDestination());
	        existingFlight.setFlightNumber(updatedFlight.getFlightNumber());
	        existingFlight.setFlightDate(updatedFlight.getFlightDate());
	        existingFlight.setSeatsAvailable(updatedFlight.getSeatsAvailable());
	        existingFlight.setFare(updatedFlight.getFare());
	        return flightRepository.save(existingFlight);
	    } else {
	        throw new RuntimeException("Flight with ID " + id + " not found.");
	    }
	}

	// ADMIN: delete flight
	public void deleteFlight(Long id) {
	    flightRepository.deleteById(id);
	}

}
