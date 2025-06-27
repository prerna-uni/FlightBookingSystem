package com.flightapp.search.controller;

import com.flightapp.search.dto.FareDTO;
import com.flightapp.search.model.Flight;
import com.flightapp.search.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fare")
@CrossOrigin
public class FareController {
	@Autowired
	private FlightRepository flightRepository;

	@GetMapping("/{flightNumber}/{flightDate}")
	public FareDTO getFare(@PathVariable String flightNumber,
	                       @PathVariable String flightDate) {
	    Flight flight = flightRepository.findByFlightNumberAndFlightDate(flightNumber, flightDate);
	    if (flight == null) {
	        return null;
	    }
	    FareDTO dto = new FareDTO();
	    dto.setFare(flight.getFare());
	    return dto;
	}

}