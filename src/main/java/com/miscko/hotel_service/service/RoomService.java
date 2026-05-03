package com.miscko.hotel_service.service;

import com.miscko.hotel_service.dto.RoomInfoResponse;
import com.miscko.hotel_service.exception.ResourceNotFoundException;
import com.miscko.hotel_service.model.Room;
import com.miscko.hotel_service.model.Tariff;
import com.miscko.hotel_service.repository.HotelRepository;
import com.miscko.hotel_service.repository.RoomRepository;
import com.miscko.hotel_service.repository.TariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final TariffRepository tariffRepository;

    public RoomService(RoomRepository roomRepository,
                       HotelRepository hotelRepository,
                       TariffRepository tariffRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.tariffRepository = tariffRepository;
    }

    public List<Room> getAll(Long hotelId) {
        if (hotelId != null) {
            return roomRepository.findByHotelId(hotelId);
        }
        return roomRepository.findAll();
    }

    public Room getById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));
    }

    @Transactional
    public Room create(Room room) {
        validateReferences(room.getHotelId(), room.getTariffId());
        return roomRepository.save(room);
    }

    @Transactional
    public Room update(Long id, Room updatedRoom) {
        Room room = getById(id);
        validateReferences(updatedRoom.getHotelId(), updatedRoom.getTariffId());

        room.setNumber(updatedRoom.getNumber());
        room.setType(updatedRoom.getType());
        room.setCapacity(updatedRoom.getCapacity());
        room.setBasePrice(updatedRoom.getBasePrice());
        room.setHotelId(updatedRoom.getHotelId());
        room.setTariffId(updatedRoom.getTariffId());

        return roomRepository.save(room);
    }

    @Transactional
    public void delete(Long id) {
        Room room = getById(id);
        roomRepository.delete(room);
    }

    public RoomInfoResponse getRoomInfo(Long roomId) {
        Room room = getById(roomId);

        Tariff tariff = tariffRepository.findById(room.getTariffId())
                .orElseThrow(() -> new ResourceNotFoundException("Tariff with id " + room.getTariffId() + " not found"));

        return new RoomInfoResponse(
                room.getId(),
                room.getHotelId(),
                room.getTariffId(),
                room.getBasePrice(),
                tariff.getMultiplier(),
                tariff.getCancellationPenaltyPercent()
        );
    }

    private void validateReferences(Long hotelId, Long tariffId) {
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel with id " + hotelId + " not found");
        }

        if (!tariffRepository.existsById(tariffId)) {
            throw new ResourceNotFoundException("Tariff with id " + tariffId + " not found");
        }
    }
}