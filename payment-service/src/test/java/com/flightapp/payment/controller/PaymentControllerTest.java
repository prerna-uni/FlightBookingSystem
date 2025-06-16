package com.flightapp.payment.controller;

import com.flightapp.payment.dto.PaymentRequest;
import com.flightapp.payment.dto.PaymentResponse;
import com.flightapp.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;
    private String bookingReference;

    @BeforeEach
    void setUp() {
        bookingReference = "BOOKREF789";

        // Setup PaymentRequest
        paymentRequest = new PaymentRequest();
        paymentRequest.setBookingReference(bookingReference);
        paymentRequest.setAmount(250.00);

        // Setup PaymentResponse
        paymentResponse = new PaymentResponse();
        paymentResponse.setOrderId("ORDER123ABC");
        paymentResponse.setRazorpayKey("rzp_test_xyz");
    }

    // --- Tests for createOrder endpoint ---
    @Test
    void testCreateOrder_Success() throws Exception {
        // Given
        // Mock the service call to return a successful PaymentResponse
        when(paymentService.createPaymentOrder(any(PaymentRequest.class))).thenReturn(paymentResponse);

        // When
        // Call the controller method
        ResponseEntity<PaymentResponse> responseEntity = paymentController.createOrder(paymentRequest);

        // Then
        // Assert that the response is not null and has OK status
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Assert that the response body matches the expected PaymentResponse
        assertNotNull(responseEntity.getBody());
        assertEquals(paymentResponse.getOrderId(), responseEntity.getBody().getOrderId());
        assertEquals(paymentResponse.getRazorpayKey(), responseEntity.getBody().getRazorpayKey());

        // Verify that the service method was called exactly once with any PaymentRequest
        verify(paymentService, times(1)).createPaymentOrder(any(PaymentRequest.class));
    }

    // --- Tests for confirmPayment endpoint ---
    @Test
    void testConfirmPayment_Success() {
        // Given
        // Mock the service call to return true for successful confirmation
        when(paymentService.confirmPayment(bookingReference)).thenReturn(true);

        // When
        // Call the controller method with a booking reference
        ResponseEntity<String> responseEntity = paymentController.confirmPayment(bookingReference);

        // Then
        // Assert that the response is not null and has OK status
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Assert that the response body is "Payment confirmed"
        assertEquals("Payment confirmed", responseEntity.getBody());

        // Verify that the service method was called exactly once with the specific bookingReference
        verify(paymentService, times(1)).confirmPayment(bookingReference);
    }

    @Test
    void testConfirmPayment_NotFound() {
        // Given
        // Mock the service call to return false, indicating payment not found
        when(paymentService.confirmPayment(bookingReference)).thenReturn(false);

        // When
        // Call the controller method with a booking reference
        ResponseEntity<String> responseEntity = paymentController.confirmPayment(bookingReference);

        // Then
        // Assert that the response is not null and has BAD_REQUEST status
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        // Assert that the response body is "Payment not found"
        assertEquals("Payment not found", responseEntity.getBody());

        // Verify that the service method was called exactly once with the specific bookingReference
        verify(paymentService, times(1)).confirmPayment(bookingReference);
    }

    // --- Tests for isPaymentSuccessful endpoint ---
    @Test
    void testIsPaymentSuccessful_True() {
        // Given
        // Mock the service call to return true
        when(paymentService.isPaymentSuccessful(bookingReference)).thenReturn(true);

        // When
        // Call the controller method
        ResponseEntity<Boolean> responseEntity = paymentController.isPaymentSuccessful(bookingReference);

        // Then
        // Assert that the response is not null and has OK status
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Assert that the response body is true
        assertEquals(true, responseEntity.getBody());

        // Verify that the service method was called exactly once
        verify(paymentService, times(1)).isPaymentSuccessful(bookingReference);
    }

    @Test
    void testIsPaymentSuccessful_False() {
        // Given
        // Mock the service call to return false
        when(paymentService.isPaymentSuccessful(bookingReference)).thenReturn(false);

        // When
        // Call the controller method
        ResponseEntity<Boolean> responseEntity = paymentController.isPaymentSuccessful(bookingReference);

        // Then
        // Assert that the response is not null and has OK status
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Assert that the response body is false
        assertEquals(false, responseEntity.getBody());

        // Verify that the service method was called exactly once
        verify(paymentService, times(1)).isPaymentSuccessful(bookingReference);
    }
}