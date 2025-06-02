package com.flightapp.checkin.controller;

import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.service.CheckinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    private static final Logger logger = LoggerFactory.getLogger(CheckinController.class);
    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmCheckin(@RequestBody BookingDTO bookingDTO) {
        logger.info("Received check-in request for booking ID: {}", bookingDTO.getId());
        String message = checkinService.performCheckin(bookingDTO);
        logger.info("Check-in result for booking ID {}: {}", bookingDTO.getId(), message);
        return ResponseEntity.ok(message);
    }
}
