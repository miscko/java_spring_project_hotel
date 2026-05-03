package com.miscko.booking_service.dto;

import java.math.BigDecimal;

public class CancelBookingResponse {

    private Long bookingId;
    private BigDecimal penalty;
    private BigDecimal refund;
    private String message;

    public CancelBookingResponse() {
    }

    public CancelBookingResponse(Long bookingId, BigDecimal penalty, BigDecimal refund, String message) {
        this.bookingId = bookingId;
        this.penalty = penalty;
        this.refund = refund;
        this.message = message;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public String getMessage() {
        return message;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}