package com.miscko.hotel_service.repository;

import com.miscko.hotel_service.model.Hotel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCityContainingIgnoreCase(String city);
    List<Hotel> findByCityContainingIgnoreCase(String city, Sort sort);
}