package com.daw.controller.dto;

import com.daw.datamodel.entities.User;

import lombok.Data;

@Data
public class RatingDTO {
	
	private Long id;
	
	private Integer rating;
	
	private User userRate;
	
	private User ratedUser;


}
