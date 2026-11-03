package com.daw.controller.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * DTO ligero de usuario para listados y busquedas: los mismos datos que {@link UserDTO}
 * pero sin los conjuntos de valoraciones recibidas y hechas, cuyo coste crece con la actividad
 * de cada usuario. El detalle completo se obtiene con {@code GET /api/user/{id}}.
 */
@Data
public class UserListDTO {

    private Long id;
    private String username;
    private String email;
    private String name;
    private LocalDate birthdate;
    private String genre;
    private String phone;
    private Integer strikes;
    private Boolean banned;
    private Integer drivingLicenseYear;
    private CampusDTO usualCampusDTO;
    private TownDTO homeTownDTO;
    private String description;
    private String profileImageUrl;
    private String role;

}
