package com.miscko.booking_service.client;

import com.miscko.booking_service.dto.RoomInfoResponse;
import com.miscko.booking_service.exception.ResourceNotFoundException;
import com.miscko.booking_service.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class HotelServiceClient {

    private final RestTemplate restTemplate;

    @Value("${hotel.service.url}")
    private String hotelServiceUrl;

    public HotelServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RoomInfoResponse getRoomInfo(Long roomId) {
        try {
            String url = hotelServiceUrl + "/api/internal/rooms/" + roomId;
            return restTemplate.getForObject(url, RoomInfoResponse.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Room with id " + roomId + " not found in hotel-service");
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("hotel-service is unavailable");
        }
    }
}