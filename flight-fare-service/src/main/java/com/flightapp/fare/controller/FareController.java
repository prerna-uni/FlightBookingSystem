package com.flightapp.fare.controller;

import com.flightapp.fare.model.Fare;
import com.flightapp.fare.repository.FareRepository;
import com.flightapp.fare.dto.FareDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/fares")
@CrossOrigin(origins = "*")  
public class FareController {

    @Autowired
    private FareRepository fareRepository;

    @GetMapping("/{flightNumber}/{flightDate}")
    public FareDTO getFare(@PathVariable String flightNumber,
                           @PathVariable String flightDate) {
        Fare fare = fareRepository.findByFlightNumberAndFlightDate(flightNumber, flightDate);
        if (fare == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fare not found");
        }

        FareDTO fareDTO = new FareDTO();
        fareDTO.setFare(fare.getFare());
        return fareDTO;
    }

    @PostMapping
    public Fare saveFare(@RequestBody Fare fare) {
        return fareRepository.save(fare);
    }
}
