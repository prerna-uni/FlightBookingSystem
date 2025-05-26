package com.flightapp.search.service;

import com.flightapp.search.dto.FareDTO;
import com.flightapp.search.dto.FlightResponse;
import com.flightapp.search.feign.FareClient;
import com.flightapp.search.model.Flight;
import com.flightapp.search.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<FlightResponse> searchFlights(String origin, String destination, String date) {
        List<Flight> flights = flightRepository.findByOriginAndDestinationAndFlightDate(origin, destination, date);

        return flights.stream().map(flight -> {
            FareDTO fareDTO = null;
            try {
                fareDTO = fareClient.getFare(flight.getFlightNumber(), flight.getFlightDate());
            } catch (Exception e) {
                // Log or handle errors (e.g., fare service down)
            }

            Double fare = (fareDTO != null) ? fareDTO.getFare() : null;

            FlightResponse response = new FlightResponse();
            response.setId(flight.getId());
            response.setOrigin(flight.getOrigin());
            response.setDestination(flight.getDestination());
            response.setFlightNumber(flight.getFlightNumber());
            response.setFlightDate(flight.getFlightDate());
            response.setSeatsAvailable(flight.getSeatsAvailable());
            response.setFare(fare);

            return response;
        }).collect(Collectors.toList());
    }
}
