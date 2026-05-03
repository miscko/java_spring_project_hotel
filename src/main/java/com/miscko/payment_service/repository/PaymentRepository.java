package com.miscko.payment_service.repository;

import com.miscko.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByBookingId(Long bookingId);
    Optional<Payment> findByBookingId(Long bookingId);
}