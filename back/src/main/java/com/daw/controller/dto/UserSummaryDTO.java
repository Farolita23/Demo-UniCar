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

}
