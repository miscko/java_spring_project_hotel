package com.miscko.payment_service.client;

import com.miscko.payment_service.dto.BookingInfoResponse;
import com.miscko.payment_service.exception.ResourceNotFoundException;
import com.miscko.payment_service.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class BookingServiceClient {

    private final RestTemplate restTemplate;

    @Value("${booking.service.url}")
    private String bookingServiceUrl;

    public BookingServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BookingInfoResponse getBookingInfo(Long bookingId) {
        try {
            String url = bookingServiceUrl + "/api/internal/bookings/" + bookingId;
            return restTemplate.getForObject(url, BookingInfoResponse.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Booking with id " + bookingId + " not found in booking-service");
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("booking-service is unavailable");
        }
    }
}