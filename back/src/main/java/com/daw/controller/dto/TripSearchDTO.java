package com.daw.controller.dto;
 
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
 
import lombok.Data;
 
@Data
public class TripSearchDTO {
 
    private Long campusId;       // campus del viaje (origen o destino según isToCampus)
    private Long townId;         // town del viaje (origen o destino según isToCampus)
    private Boolean isToCampus;  // true = hacia campus, false = desde campus (opcional)
 
    private LocalDate departureDate;
    private LocalTime departureTime;
 
    private BigDecimal maxPrice;
 
    private Integer minFreeSeats; // mínimo huecos libres requeridos
}