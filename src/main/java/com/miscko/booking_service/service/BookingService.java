package com.miscko.booking_service.service;

import com.miscko.booking_service.client.HotelServiceClient;
import com.miscko.booking_service.dto.BookingInfoResponse;
import com.miscko.booking_service.dto.BookingRequest;
import com.miscko.booking_service.dto.CancelBookingResponse;
import com.miscko.booking_service.dto.RoomInfoResponse;
import com.miscko.booking_service.exception.BusinessException;
import com.miscko.booking_service.exception.ResourceNotFoundException;
import com.miscko.booking_service.model.Booking;
import com.miscko.booking_service.model.BookingStatus;
import com.miscko.booking_service.repository.BookingRepository;
import com.miscko.booking_service.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final HotelServiceClient hotelServiceClient;

    public BookingService(BookingRepository bookingRepository,
                          ClientRepository clientRepository,
                          HotelServiceClient hotelServiceClient) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
        this.hotelServiceClient = hotelServiceClient;
    }

    public List<Booking> getAll(Long clientId) {
        if (clientId != null) {
            if (!clientRepository.existsById(clientId)) {
                throw new ResourceNotFoundException("Client with id " + clientId + " not found");
            }
            return bookingRepository.findByClientId(clientId);
        }
        return bookingRepository.findAll();
    }

    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " not found"));
    }

    public List<Booking> getClientHistory(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client with id " + clientId + " not found");
        }
        return bookingRepository.findByClientId(clientId);
    }

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        hotelServiceClient.getRoomInfo(roomId);

        List<Booking> bookings = bookingRepository.findByRoomIdAndStatusNot(roomId, BookingStatus.CANCELLED);

        for (Booking booking : bookings) {
            boolean overlaps = checkIn.isBefore(booking.getCheckOut()) && checkOut.isAfter(booking.getCheckIn());
            if (overlaps) {
                return false;
            }
        }

        return true;
    }

    @Transactional
    public Booking create(BookingRequest request) {
        validateBookingRequest(request);

        RoomInfoResponse roomInfo = hotelServiceClient.getRoomInfo(request.getRoomId());

        if (!isRoomAvailable(request.getRoomId(), request.getCheckIn(), request.getCheckOut())) {
            throw new BusinessException("Room is not available for the selected period");
        }

        Booking booking = new Booking();
        booking.setClientId(request.getClientId());
        booking.setRoomId(request.getRoomId());
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setTotalPrice(calculateTotalPrice(roomInfo, request.getCheckIn(), request.getCheckOut()));
        booking.setStatus(BookingStatus.CREATED);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking update(Long id, BookingRequest request) {
        Booking booking = getById(id);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Cancelled booking cannot be updated");
        }

        validateBookingRequest(request);

        RoomInfoResponse roomInfo = hotelServiceClient.getRoomInfo(request.getRoomId());

        List<Booking> bookings = bookingRepository.findByRoomIdAndStatusNotAndIdNot(
                request.getRoomId(),
                BookingStatus.CANCELLED,
                id
        );

        for (Booking existingBooking : bookings) {
            boolean overlaps = request.getCheckIn().isBefore(existingBooking.getCheckOut())
                    && request.getCheckOut().isAfter(existingBooking.getCheckIn());
            if (overlaps) {
                throw new BusinessException("Room is not available for the selected period");
            }
        }

        booking.setClientId(request.getClientId());
        booking.setRoomId(request.getRoomId());
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setTotalPrice(calculateTotalPrice(roomInfo, request.getCheckIn(), request.getCheckOut()));

        return bookingRepository.save(booking);
    }

    @Transactional
    public CancelBookingResponse cancel(Long bookingId) {
        Booking booking = getById(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Booking is already cancelled");
        }

        RoomInfoResponse roomInfo = hotelServiceClient.getRoomInfo(booking.getRoomId());

        BigDecimal penalty = booking.getTotalPrice()
                .multiply(BigDecimal.valueOf(roomInfo.getCancellationPenaltyPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal refund = booking.getTotalPrice().subtract(penalty);

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        return new CancelBookingResponse(
                booking.getId(),
                penalty,
                refund,
                "Booking cancelled successfully"
        );
    }

    @Transactional
    public void delete(Long id) {
        Booking booking = getById(id);
        bookingRepository.delete(booking);
    }

    public BookingInfoResponse getBookingInfo(Long id) {
        Booking booking = getById(id);
        return new BookingInfoResponse(
                booking.getId(),
                booking.getTotalPrice(),
                booking.getStatus().name()
        );
    }

    private void validateBookingRequest(BookingRequest request) {
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new BusinessException("Check-out date must be after check-in date");
        }

        if (!clientRepository.existsById(request.getClientId())) {
            throw new ResourceNotFoundException("Client with id " + request.getClientId() + " not found");
        }
    }

    private BigDecimal calculateTotalPrice(RoomInfoResponse roomInfo, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        return roomInfo.getBasePrice()
                .multiply(BigDecimal.valueOf(nights))
                .multiply(roomInfo.getMultiplier())
                .setScale(2, RoundingMode.HALF_UP);
    }
}