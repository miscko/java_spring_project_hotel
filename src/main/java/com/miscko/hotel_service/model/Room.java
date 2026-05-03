package com.miscko.hotel_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room number is required")
    private String number;

    @NotBlank(message = "Room type is required")
    private String type;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal basePrice;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;

    @NotNull(message = "Tariff ID is required")
    private Long tariffId;

    public Room() {
    }

    public Room(Long id, String number, String type, Integer capacity, BigDecimal basePrice, Long hotelId, Long tariffId) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.hotelId = hotelId;
        this.tariffId = tariffId;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public Long getTariffId() {
        return tariffId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public void setTariffId(Long tariffId) {
        this.tariffId = tariffId;
    }
}