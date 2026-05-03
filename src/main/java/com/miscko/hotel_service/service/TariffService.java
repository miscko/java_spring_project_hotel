package com.miscko.hotel_service.service;

import com.miscko.hotel_service.exception.BusinessException;
import com.miscko.hotel_service.exception.ResourceNotFoundException;
import com.miscko.hotel_service.model.Tariff;
import com.miscko.hotel_service.repository.RoomRepository;
import com.miscko.hotel_service.repository.TariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final RoomRepository roomRepository;

    public TariffService(TariffRepository tariffRepository, RoomRepository roomRepository) {
        this.tariffRepository = tariffRepository;
        this.roomRepository = roomRepository;
    }

    public List<Tariff> getAll() {
        return tariffRepository.findAll();
    }

    public Tariff getById(Long id) {
        return tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff with id " + id + " not found"));
    }

    @Transactional
    public Tariff create(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    @Transactional
    public Tariff update(Long id, Tariff updatedTariff) {
        Tariff tariff = getById(id);
        tariff.setName(updatedTariff.getName());
        tariff.setSeason(updatedTariff.getSeason());
        tariff.setMultiplier(updatedTariff.getMultiplier());
        tariff.setCancellationPenaltyPercent(updatedTariff.getCancellationPenaltyPercent());
        return tariffRepository.save(tariff);
    }

    @Transactional
    public void delete(Long id) {
        Tariff tariff = getById(id);

        if (roomRepository.existsByTariffId(id)) {
            throw new BusinessException("Cannot delete tariff that is assigned to rooms");
        }

        tariffRepository.delete(tariff);
    }
}