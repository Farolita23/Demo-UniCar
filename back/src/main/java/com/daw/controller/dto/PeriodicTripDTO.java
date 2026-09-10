package com.daw.controller.dto;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import lombok.Data;

@Data
public class PeriodicTripDTO {

    private Long id;

    private CarDTO carDTO;

    private CampusDTO campusDTO;

    private TownDTO townDTO;

    private Boolean isToCampus;

    private String departureAddress;

    private BigDecimal price;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime departureTime;

    private Set<DayOfWeek> daysOfWeek;

    private Integer repeatIntervalWeeks;

    private int tripsGenerated;
}
