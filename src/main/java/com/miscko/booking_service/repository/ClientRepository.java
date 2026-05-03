package com.miscko.booking_service.repository;

import com.miscko.booking_service.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}