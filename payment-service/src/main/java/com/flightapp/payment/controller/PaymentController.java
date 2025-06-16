package com.flightapp.payment.controller;

import com.flightapp.payment.dto.PaymentRequest;
import com.flightapp.payment.dto.PaymentResponse;
import com.flightapp.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody PaymentRequest request) throws Exception {
        return ResponseEntity.ok(paymentService.createPaymentOrder(request));
    }

    @PutMapping("/confirm/{bookingReference}")
    public ResponseEntity<String> confirmPayment(@PathVariable String bookingReference) {
        boolean success = paymentService.confirmPayment(bookingReference);
        return success ? ResponseEntity.ok("Payment confirmed") : ResponseEntity.badRequest().body("Payment not found");
    }

    @GetMapping("/verify/{bookingReference}")
    public ResponseEntity<Boolean> isPaymentSuccessful(@PathVariable String bookingReference) {
        return ResponseEntity.ok(paymentService.isPaymentSuccessful(bookingReference));
    }
}
