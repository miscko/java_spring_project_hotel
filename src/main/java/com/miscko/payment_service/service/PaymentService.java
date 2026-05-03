package com.miscko.payment_service.service;

import com.miscko.payment_service.client.BookingServiceClient;
import com.miscko.payment_service.dto.BookingInfoResponse;
import com.miscko.payment_service.exception.BusinessException;
import com.miscko.payment_service.exception.ResourceNotFoundException;
import com.miscko.payment_service.model.Payment;
import com.miscko.payment_service.model.PaymentStatus;
import com.miscko.payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingServiceClient bookingServiceClient;

    public PaymentService(PaymentRepository paymentRepository, BookingServiceClient bookingServiceClient) {
        this.paymentRepository = paymentRepository;
        this.bookingServiceClient = bookingServiceClient;
    }

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with id " + id + " not found"));
    }

    @Transactional
    public Payment create(Long bookingId) {
        BookingInfoResponse bookingInfo = bookingServiceClient.getBookingInfo(bookingId);

        if (paymentRepository.existsByBookingId(bookingId)) {
            throw new BusinessException("Payment already exists for booking with id " + bookingId);
        }

        if ("CANCELLED".equalsIgnoreCase(bookingInfo.getStatus())) {
            throw new BusinessException("Cannot create payment for cancelled booking");
        }

        Payment payment = new Payment();
        payment.setBookingId(bookingInfo.getBookingId());
        payment.setAmount(bookingInfo.getTotalPrice());
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment update(Long id, Payment updatedPayment) {
        Payment payment = getById(id);

        BookingInfoResponse bookingInfo = bookingServiceClient.getBookingInfo(updatedPayment.getBookingId());

        if (!payment.getBookingId().equals(updatedPayment.getBookingId())
                && paymentRepository.existsByBookingId(updatedPayment.getBookingId())) {
            throw new BusinessException("Another payment already exists for booking with id " + updatedPayment.getBookingId());
        }

        if ("CANCELLED".equalsIgnoreCase(bookingInfo.getStatus()) && updatedPayment.getStatus() == PaymentStatus.PAID) {
            throw new BusinessException("Cancelled booking cannot have PAID status");
        }

        payment.setBookingId(updatedPayment.getBookingId());
        payment.setAmount(updatedPayment.getAmount());
        payment.setStatus(updatedPayment.getStatus());

        if (updatedPayment.getPaymentDate() != null) {
            payment.setPaymentDate(updatedPayment.getPaymentDate());
        }

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment refundByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment for booking with id " + bookingId + " not found"));

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Transactional
    public void delete(Long id) {
        Payment payment = getById(id);
        paymentRepository.delete(payment);
    }
}