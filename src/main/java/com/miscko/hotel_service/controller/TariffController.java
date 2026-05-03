package com.miscko.hotel_service.controller;

import com.miscko.hotel_service.model.Tariff;
import com.miscko.hotel_service.service.TariffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(tariffService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tariff> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tariffService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Tariff> create(@Valid @RequestBody Tariff tariff) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tariffService.create(tariff));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tariff> update(@PathVariable Long id, @Valid @RequestBody Tariff tariff) {
        return ResponseEntity.ok(tariffService.update(id, tariff));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tariffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}