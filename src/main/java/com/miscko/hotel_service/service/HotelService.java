package com.miscko.hotel_service.service;

import com.miscko.hotel_service.exception.BusinessException;
import com.miscko.hotel_service.exception.ResourceNotFoundException;
import com.miscko.hotel_service.model.Hotel;
import com.miscko.hotel_service.repository.HotelRepository;
import com.miscko.hotel_service.repository.RoomRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public HotelService(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public List<Hotel> getAll(String city, String sortBy) {
        Sort sort = (sortBy != null && !sortBy.isBlank()) ? Sort.by(sortBy) : Sort.unsorted();

        if (city != null && !city.isBlank()) {
            return hotelRepository.findByCityContainingIgnoreCase(city, sort);
        }

        return hotelRepository.findAll(sort);
    }

    public Hotel getById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + id + " not found"));
    }

    @Transactional
    public Hotel create(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Transactional
    public Hotel update(Long id, Hotel updatedHotel) {
        Hotel hotel = getById(id);
        hotel.setName(updatedHotel.getName());
        hotel.setCity(updatedHotel.getCity());
        hotel.setAddress(updatedHotel.getAddress());
        hotel.setRating(updatedHotel.getRating());
        return hotelRepository.save(hotel);
    }

    @Transactional
    public void delete(Long id) {
        Hotel hotel = getById(id);

        if (roomRepository.existsByHotelId(id)) {
            throw new BusinessException("Cannot delete hotel with existing rooms");
        }

        hotelRepository.delete(hotel);
    }
}