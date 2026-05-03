package com.miscko.hotel_service.controller;

import com.miscko.hotel_service.dto.RoomInfoResponse;
import com.miscko.hotel_service.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/rooms")
public class InternalRoomController {

    private final RoomService roomService;

    public InternalRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomInfoResponse> getRoomInfo(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomInfo(id));
    }
}