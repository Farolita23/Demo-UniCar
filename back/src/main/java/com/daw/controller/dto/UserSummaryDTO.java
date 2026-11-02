package com.daw.controller.dto;

import lombok.Data;

/**
 * DTO reducido de usuario para evitar referencias circulares
 * cuando se embebe dentro de CarDTO, TripDTO (passengers/requesters), etc.
 */
@Data
public class UserSummaryDTO {

    private Long id;

    private String username;

    private String name;

    private String profileImageUrl;


    /** Valoración media recibida (null si aún no tiene valoraciones). Solo se rellena en el conductor de los viajes. */
    private Double avgRating;

    /** Número de valoraciones recibidas. Solo se rellena en el conductor de los viajes. */
    private Long ratingsCount;
}
