package com.flightapp.search.repository;

import com.flightapp.search.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {
    List<Flight> findByOriginAndDestinationAndFlightDate(String origin, String destination, String flightDate);
    Flight findByFlightNumberAndFlightDate(String flightNumber, String flightDate);
}
