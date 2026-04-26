package com.daw.controller.dto;

import lombok.Data;

@Data
public class RatingDTO {

	private Long id;

	private Integer rating;

	private UserSummaryDTO userRateDTO;

	private UserSummaryDTO ratedUserDTO;

}
