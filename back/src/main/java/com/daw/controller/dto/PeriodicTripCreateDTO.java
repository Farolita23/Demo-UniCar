package com.daw.controller.dto;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PeriodicTripCreateDTO {

    @NotNull
    private Long idCar;

    @NotNull
    private Long idCampus;

    @NotNull
    private Long idTown;

    @NotNull
    private Boolean isToCampus;

    @NotBlank
    private String departureAddress;

    @NotNull
    private BigDecimal price;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalTime departureTime;

    @NotEmpty
    private Set<DayOfWeek> daysOfWeek;

    @NotNull
    @Min(1)
    private Integer repeatIntervalWeeks = 1;
}
