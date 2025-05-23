package com.flightapp.fare.repository;

import com.flightapp.fare.model.Fare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FareRepository extends JpaRepository<Fare, Long> {
Fare findByFlightNumberAndFlightDate(String flightNumber, String flightDate);
}