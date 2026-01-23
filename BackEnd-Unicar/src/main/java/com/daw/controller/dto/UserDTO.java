package com.daw.controller.dto;

import java.time.LocalDate;
import java.util.Set;

import com.daw.datamodel.entities.Campus;
import com.daw.datamodel.entities.Car;
import com.daw.datamodel.entities.Rating;
import com.daw.datamodel.entities.Report;
import com.daw.datamodel.entities.Town;
import com.daw.datamodel.entities.Trip;

import lombok.Data;

@Data
public class UserDTO {
	
	private Long id;
	
	private String email;
	
	private String name;
	
	private LocalDate birthdate;
	
	private String genre;
	
	private String phone;
	
	private Integer strikes;
	
	private Boolean banned;
	
	private Integer drivingLicenseYear;
	
	private Campus usualCampus;
	
	private Town homeTown;
	
    private Set<Report> reportsDone;
    
    private Set<Report> reportsReceived;

    private Set<Car> cars;

    private String description;

    private Set<Trip> tripsAsAPassenger;

    private Set<Rating> ratingsReceived;

    private Set<Rating> ratingsDone;

}
