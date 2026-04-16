package com.daw.controller.dto;

import lombok.Data;

@Data
public class FavoriteDTO {
    private Long id;
    private UserSummaryDTO userDTO;
    private UserSummaryDTO favoriteUserDTO;
}
