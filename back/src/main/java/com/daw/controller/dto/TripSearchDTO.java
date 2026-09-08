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

    /**
     * Filtro por tipo de viaje según su origen:
     * {@code null} = todos, {@code true} = solo viajes de una serie periódica,
     * {@code false} = solo viajes puntuales (no generados por una periódica).
     */
    private Boolean periodic;
}