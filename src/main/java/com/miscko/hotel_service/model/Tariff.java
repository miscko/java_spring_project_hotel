package com.miscko.hotel_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tariffs")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tariff name is required")
    private String name;

    @NotBlank(message = "Season is required")
    private String season;

    @NotNull(message = "Multiplier is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Multiplier must be greater than 0")
    private BigDecimal multiplier;

    @Min(value = 0, message = "Penalty percent must be at least 0")
    @Max(value = 100, message = "Penalty percent must be at most 100")
    private Integer cancellationPenaltyPercent;

    public Tariff() {
    }

    public Tariff(Long id, String name, String season, BigDecimal multiplier, Integer cancellationPenaltyPercent) {
        this.id = id;
        this.name = name;
        this.season = season;
        this.multiplier = multiplier;
        this.cancellationPenaltyPercent = cancellationPenaltyPercent;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSeason() {
        return season;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public Integer getCancellationPenaltyPercent() {
        return cancellationPenaltyPercent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public void setCancellationPenaltyPercent(Integer cancellationPenaltyPercent) {
        this.cancellationPenaltyPercent = cancellationPenaltyPercent;
    }
}