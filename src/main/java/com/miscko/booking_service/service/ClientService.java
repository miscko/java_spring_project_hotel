package com.miscko.booking_service.service;

import com.miscko.booking_service.exception.BusinessException;
import com.miscko.booking_service.exception.ResourceNotFoundException;
import com.miscko.booking_service.model.Client;
import com.miscko.booking_service.repository.BookingRepository;
import com.miscko.booking_service.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;

    public ClientService(ClientRepository clientRepository, BookingRepository bookingRepository) {
        this.clientRepository = clientRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
    }

    @Transactional
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    @Transactional
    public Client update(Long id, Client updatedClient) {
        Client client = getById(id);
        client.setFullName(updatedClient.getFullName());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        return clientRepository.save(client);
    }

    @Transactional
    public void delete(Long id) {
        Client client = getById(id);

        if (bookingRepository.existsByClientId(id)) {
            throw new BusinessException("Cannot delete client with existing bookings");
        }

        clientRepository.delete(client);
    }
}