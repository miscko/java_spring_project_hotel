package com.miscko.hotel_service.repository;

import com.miscko.hotel_service.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    boolean existsByHotelId(Long hotelId);
    boolean existsByTariffId(Long tariffId);
}