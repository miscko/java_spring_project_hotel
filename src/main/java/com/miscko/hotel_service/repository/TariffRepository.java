package com.miscko.hotel_service.repository;

import com.miscko.hotel_service.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
}