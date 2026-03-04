package com.daw.controller.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;

@Data
public class UserDTO {
	
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
	
    private Set<ReportDTO> reportsDoneDTO;
    
    private Set<ReportDTO> reportsReceivedDTO;

    private Set<CarDTO> carsDTO;

    private String description;

    private Set<TripDTO> tripsAsAPassengerDTO;

    private Set<RatingDTO> ratingsReceivedDTO;

    private Set<RatingDTO> ratingsDoneDTO;

}
