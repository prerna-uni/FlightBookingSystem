package com.flightapp.fare.controller;

import com.flightapp.fare.model.Fare;
import com.flightapp.fare.repository.FareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fares")
public class FareController {
	@Autowired
	private FareRepository fareRepository;

	@GetMapping("/{flightNumber}/{flightDate}")
	public Fare getFare(@PathVariable String flightNumber,
	                    @PathVariable String flightDate) {
	    return fareRepository.findByFlightNumberAndFlightDate(flightNumber, flightDate);
	}

	@PostMapping
	public Fare saveFare(@RequestBody Fare fare) {
	    return fareRepository.save(fare);
	}


}
