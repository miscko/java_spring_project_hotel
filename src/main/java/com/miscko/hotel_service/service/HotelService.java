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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public HotelService(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    // Кешуємо результати пошуку списку готелів
    @Cacheable(value = "hotels")
    public List<Hotel> getAll(String city, String sortBy) {
        Sort sort = (sortBy != null && !sortBy.isBlank()) ? Sort.by(sortBy) : Sort.unsorted();

        if (city != null && !city.isBlank()) {
            return hotelRepository.findByCityContainingIgnoreCase(city, sort);
        }

        return hotelRepository.findAll(sort);
    }

    // Кешуємо конкретний готель за його id
    @Cacheable(value = "hotel", key = "#id")
    public Hotel getById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + id + " not found"));
    }

    // При створенні нового готелю очищаємо кеш списку готелів
    @Transactional
    @CacheEvict(value = "hotels", allEntries = true)
    public Hotel create(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    // При оновленні очищаємо кеш і списку, і конкретного готелю
    @Transactional
    @CacheEvict(value = {"hotels", "hotel"}, allEntries = true)
    public Hotel update(Long id, Hotel updatedHotel) {
        Hotel hotel = getById(id);
        hotel.setName(updatedHotel.getName());
        hotel.setCity(updatedHotel.getCity());
        hotel.setAddress(updatedHotel.getAddress());
        hotel.setRating(updatedHotel.getRating());
        return hotelRepository.save(hotel);
    }

    // При видаленні також очищаємо обидва кеші
    @Transactional
    @CacheEvict(value = {"hotels", "hotel"}, allEntries = true)
    public void delete(Long id) {
        Hotel hotel = getById(id);

        if (roomRepository.existsByHotelId(id)) {
            throw new BusinessException("Cannot delete hotel with existing rooms");
        }

        hotelRepository.delete(hotel);
    }
}