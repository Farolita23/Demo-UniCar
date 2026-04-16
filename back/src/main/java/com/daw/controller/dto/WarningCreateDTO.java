package com.daw.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarningCreateDTO {
    @NotBlank
    private String subject;

    @NotBlank
    private String message;

    @NotNull
    private Long idUser;
}
