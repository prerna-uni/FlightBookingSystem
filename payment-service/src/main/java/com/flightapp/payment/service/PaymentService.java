package com.flightapp.payment.service;

import com.flightapp.payment.dto.PaymentRequest;
import com.flightapp.payment.dto.PaymentResponse;
import com.flightapp.payment.model.Payment;
import com.flightapp.payment.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.flightapp.payment.client.BookingClient;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;
    private final BookingClient bookingClient;

    @Value("${razorpay.key}")
    private String razorpayKey;

    public PaymentService(PaymentRepository paymentRepository, RazorpayClient razorpayClient, BookingClient bookingClient) {
        this.paymentRepository = paymentRepository;
        this.razorpayClient = razorpayClient;
        this.bookingClient = bookingClient;
    }

    public PaymentResponse createPaymentOrder(PaymentRequest request) throws Exception {
        JSONObject options = new JSONObject();
        options.put("amount", request.getAmount() * 100); // amount in paise
        options.put("currency", "INR");
        options.put("receipt", request.getBookingReference());

        Order order = razorpayClient.orders.create(options);

        Payment payment = new Payment();
        payment.setBookingReference(request.getBookingReference());
        payment.setAmount(request.getAmount());
        payment.setPaymentId(order.get("id"));
        payment.setStatus(false);
        paymentRepository.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setOrderId(order.get("id"));
        response.setRazorpayKey(razorpayKey);

        return response;
    }

    public boolean confirmPayment(String bookingReference) {
        Payment payment = paymentRepository.findByBookingReference(bookingReference);
        if (payment != null && payment.getPaymentId() != null) {
            payment.setStatus(true);
            paymentRepository.save(payment);

            // Notify booking service
            bookingClient.updatePaymentStatus(bookingReference);

            return true;
        }
        return false;
    }


    public boolean isPaymentSuccessful(String bookingReference) {
        Payment payment = paymentRepository.findByBookingReference(bookingReference);
        return payment != null && payment.isStatus();
    }
}
