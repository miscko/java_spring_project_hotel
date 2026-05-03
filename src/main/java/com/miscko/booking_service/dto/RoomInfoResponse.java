package com.miscko.booking_service.dto;

import java.math.BigDecimal;

public class RoomInfoResponse {

    private Long roomId;
    private Long hotelId;
    private Long tariffId;
    private BigDecimal basePrice;
    private BigDecimal multiplier;
    private Integer cancellationPenaltyPercent;

    public RoomInfoResponse() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public Long getTariffId() {
        return tariffId;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public Integer getCancellationPenaltyPercent() {
        return cancellationPenaltyPercent;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public void setTariffId(Long tariffId) {
        this.tariffId = tariffId;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public void setCancellationPenaltyPercent(Integer cancellationPenaltyPercent) {
        this.cancellationPenaltyPercent = cancellationPenaltyPercent;
    }
}