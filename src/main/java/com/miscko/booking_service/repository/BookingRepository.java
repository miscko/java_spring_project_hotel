package com.miscko.booking_service.repository;

import com.miscko.booking_service.model.Booking;
import com.miscko.booking_service.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByClientId(Long clientId);
    List<Booking> findByRoomIdAndStatusNot(Long roomId, BookingStatus status);
    List<Booking> findByRoomIdAndStatusNotAndIdNot(Long roomId, BookingStatus status, Long id);
    boolean existsByClientId(Long clientId);
}