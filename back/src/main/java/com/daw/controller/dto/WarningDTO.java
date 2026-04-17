package com.daw.controller.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WarningDTO {
    private Long id;
    private String subject;
    private String message;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private UserSummaryDTO userDTO;
    private UserSummaryDTO adminDTO;
}
