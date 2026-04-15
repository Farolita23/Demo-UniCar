package com.daw.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String name;

    @NotNull
    private String phone;

    private String description;

    private String profileImageUrl;

    private Long idUsualCampus;

    private Long idHomeTown;

    private Integer drivingLicenseYear;

    // password optional on update
    private String password;
}
