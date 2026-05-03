package com.miscko.booking_service.dto;

import java.math.BigDecimal;

public class BookingInfoResponse {

    private Long bookingId;
    private BigDecimal totalPrice;
    private String status;

    public BookingInfoResponse() {
    }

    public BookingInfoResponse(Long bookingId, BigDecimal totalPrice, String status) {
        this.bookingId = bookingId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}