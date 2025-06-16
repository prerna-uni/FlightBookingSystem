package com.flightapp.payment.service;

import com.flightapp.payment.client.BookingClient;
import com.flightapp.payment.dto.PaymentRequest;
import com.flightapp.payment.dto.PaymentResponse;
import com.flightapp.payment.model.Payment;
import com.flightapp.payment.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.OrderClient;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*; // Ensure this import is present for lenient()

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RazorpayClient razorpayClient;

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private PaymentService paymentService;

    // Test data
    private String bookingReference;
    private Double amount;
    private String orderId;
    private String razorpayKey;
    private PaymentRequest paymentRequest;
    private Payment payment;
    private Order mockRazorpayOrder; // Mock for Razorpay's Order object

    @BeforeEach
    void setUp() throws RazorpayException {
        // Initialize common data
        bookingReference = "BOOKREF001";
        amount = 100.00;
        orderId = "order_xyz123";
        razorpayKey = "rzp_test_mockkey";

        // Inject the @Value field using ReflectionTestUtils
        ReflectionTestUtils.setField(paymentService, "razorpayKey", razorpayKey);

        // Setup PaymentRequest
        paymentRequest = new PaymentRequest();
        paymentRequest.setBookingReference(bookingReference);
        paymentRequest.setAmount(amount);

        // Setup Payment model
        payment = new Payment();
        payment.setId(1L);
        payment.setBookingReference(bookingReference);
        payment.setAmount(amount);
        payment.setPaymentId(orderId);
        payment.setStatus(false);

        // Mock Razorpay Order behavior
        mockRazorpayOrder = mock(Order.class);
        // Mark this stubbing as lenient
        lenient().when(mockRazorpayOrder.get("id")).thenReturn(orderId); // LINE 76 (now with lenient())

        OrderClient ordersMock = mock(OrderClient.class);
        ReflectionTestUtils.setField(razorpayClient, "orders", ordersMock);
        // Mark this stubbing as lenient
        lenient().when(ordersMock.create(any(JSONObject.class))).thenReturn(mockRazorpayOrder); // LINE 80 (now with lenient())
    }

    // --- Tests for createPaymentOrder method ---

    @Test
    void testCreatePaymentOrder_Success() throws Exception {
        // Given
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When
        PaymentResponse response = paymentService.createPaymentOrder(paymentRequest);

        // Then
        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(razorpayKey, response.getRazorpayKey());

        verify(razorpayClient.orders, times(1)).create(any(JSONObject.class));

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(bookingReference, savedPayment.getBookingReference());
        assertEquals(amount, savedPayment.getAmount());
        assertEquals(orderId, savedPayment.getPaymentId());
        assertFalse(savedPayment.isStatus());
    }

    @Test
    void testCreatePaymentOrder_RazorpayException() throws Exception {
        // Given
        // Retrieve the ordersMock which was set via ReflectionTestUtils in setUp
        OrderClient ordersMock = (OrderClient) ReflectionTestUtils.getField(razorpayClient, "orders");
        // This specific stubbing *overrides* the lenient one for this test
        when(ordersMock.create(any(JSONObject.class))).thenThrow(new RazorpayException("Razorpay API error"));

        // When / Then
        Exception exception = assertThrows(RazorpayException.class, () ->
                paymentService.createPaymentOrder(paymentRequest)
        );
        assertEquals("Razorpay API error", exception.getMessage());

        verify(paymentRepository, never()).save(any(Payment.class));
    }


    // --- Tests for confirmPayment method ---

    @Test
    void testConfirmPayment_Success() {
        // Given
        when(paymentRepository.findByBookingReference(bookingReference)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        
        // This stubbing is now fine as it's the only one for this method within this test
        when(bookingClient.updatePaymentStatus(bookingReference)).thenReturn(null);

        assertFalse(payment.isStatus());

        // When
        boolean result = paymentService.confirmPayment(bookingReference);

        // Then
        assertTrue(result);
        assertTrue(payment.isStatus());

        verify(paymentRepository, times(1)).findByBookingReference(bookingReference);
        verify(paymentRepository, times(1)).save(payment);
        verify(bookingClient, times(1)).updatePaymentStatus(bookingReference);
    }

    @Test
    void testConfirmPayment_PaymentNotFound_ReturnsFalse() {
        // Given
        when(paymentRepository.findByBookingReference(bookingReference)).thenReturn(null);

        // When
        boolean result = paymentService.confirmPayment(bookingReference);

        // Then
        assertFalse(result);

        verify(paymentRepository, times(1)).findByBookingReference(bookingReference);
        verify(paymentRepository, never()).save(any(Payment.class));
        verifyNoInteractions(bookingClient);
    }

    @Test
    void testConfirmPayment_PaymentFoundButIdIsNull_ReturnsFalse() {
        // Given
        payment.setPaymentId(null);
        when(paymentRepository.findByBookingReference(bookingReference)).thenReturn(payment);

        // When
        boolean result = paymentService.confirmPayment(bookingReference);

        // Then
        assertFalse(result);

        verify(paymentRepository, times(1)).findByBookingReference(bookingReference);
        verify(paymentRepository, never()).save(any(Payment.class));
        verifyNoInteractions(bookingClient);
    }

    // --- Tests for isPaymentSuccessful method ---

    @Test
    void testIsPaymentSuccessful_True() {
        // Given
        payment.setStatus(true);
        when(paymentRepository.findByBookingReference(bookingReference)).thenReturn(payment);

        // When
        boolean result = paymentService.isPaymentSuccessful(bookingReference);

        // Then
        assertTrue(result);

        verify(paymentRepository, times(1)).findByBookingReference(bookingReference);
    }

    @Test
    void testIsPaymentSuccessful_False_PaymentStatusIsFalse() {
        // Given
        payment.setStatus(false);
        when(paymentRepository.findByBookingReference(bookingReference)).thenReturn(payment);

        // When
        boolean result = paymentService.isPaymentSuccessful(bookingReference);

        // Then
        assertFalse(result);

        verify(paymentRepository, times(1)).findByBookingReference(bookingReference);
    }

    @Test
    void testIsPaymentSuccessful_False_PaymentNotFound() {
        // Given
        when(paymentRepository.findByBookingReference(bookingReference)).thenReturn(null);

        // When
        boolean result = paymentService.isPaymentSuccessful(bookingReference);

        // Then
        assertFalse(result);

        verify(paymentRepository, times(1)).findByBookingReference(bookingReference);
    }
}