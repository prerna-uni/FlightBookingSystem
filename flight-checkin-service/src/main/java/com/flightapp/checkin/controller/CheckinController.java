package com.flightapp.checkin.controller;

import com.flightapp.checkin.dto.BookingDTO;
import com.flightapp.checkin.service.CheckinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmCheckin(@RequestBody BookingDTO bookingDTO) {
        String message = checkinService.performCheckin(bookingDTO);
        return ResponseEntity.ok(message);
    }
}
