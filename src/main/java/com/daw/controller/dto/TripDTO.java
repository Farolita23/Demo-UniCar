package com.daw.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import lombok.Data;

@Data
public class TripDTO {

    private Long id;

    private CarDTO carDTO;

    private CampusDTO campusDTO;

    private TownDTO townDTO;

    private Boolean isToCampus;

    private LocalDate departureDate;

    private LocalTime departureTime;

    private String departureAddress;

    private BigDecimal price;

    private UserSummaryDTO driverDTO;

    // Usamos UserSummaryDTO para evitar referencia circular User <-> Car
    private Set<UserSummaryDTO> passengersDTO;

    private Set<UserSummaryDTO> requestersDTO;

}
