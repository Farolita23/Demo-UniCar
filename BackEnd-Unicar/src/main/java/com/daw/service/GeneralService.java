package com.daw.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.datamodel.entities.Campus;
import com.daw.datamodel.entities.Car;
import com.daw.datamodel.entities.Rating;
import com.daw.datamodel.entities.Report;
import com.daw.datamodel.entities.Town;
import com.daw.datamodel.entities.Trip;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.CampusRepository;
import com.daw.datamodel.repository.CarRepository;
import com.daw.datamodel.repository.RatingRepository;
import com.daw.datamodel.repository.ReportRepository;
import com.daw.datamodel.repository.TownRepository;
import com.daw.datamodel.repository.TripRepository;
import com.daw.datamodel.repository.UserRepository;
import com.daw.exceptions.CampusNotFoundException;
import com.daw.exceptions.CarNotFoundException;
import com.daw.exceptions.RatingNotFoundException;
import com.daw.exceptions.ReportNotFoundException;
import com.daw.exceptions.TownNotFoundException;
import com.daw.exceptions.TripNotFoundException;
import com.daw.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneralService {
	
	private final CampusRepository campusRepository;
	private final CarRepository carRepository;
	private final RatingRepository ratingRepository;
	private final ReportRepository reportRepository;
	private final TownRepository townRepository;
	private final TripRepository tripRepository;
	private final UserRepository userRepository;
	
	public Campus findCampusById(Long id) {
		Optional<Campus> campus = campusRepository.findById(id);
		if(campus.isPresent()) {
			return campus.get();
		} else {
			throw new CampusNotFoundException(id);
		}
	}
	
	public Car findCarById(Long id) {
		Optional<Car> car = carRepository.findById(id);
		if(car.isPresent()) {
			return car.get();
		} else {
			throw new CarNotFoundException(id);
		}
	}
	
	public Rating findRatingById(Long id) {
		Optional<Rating> rating = ratingRepository.findById(id);
		if(rating.isPresent()) {
			return rating.get();
		} else {
			throw new RatingNotFoundException(id);
		}
	}
	
	public Report findReportById(Long id) {
		Optional<Report> report = reportRepository.findById(id);
		if(report.isPresent()) {
			return report.get();
		} else {
			throw new ReportNotFoundException(id);
		}
	}
	
	public Town findTownById(Long id) {
		Optional<Town> town = townRepository.findById(id);
		if(town.isPresent()) {
			return town.get();
		} else {
			throw new TownNotFoundException(id);
		}
	}
	
	public Trip findTripById(Long id) {
		Optional<Trip> trip = tripRepository.findById(id);
		if(trip.isPresent()) {
			return trip.get();
		} else {
			throw new TripNotFoundException(id);
		}
	}
	
	public User findUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return user.get();
		} else {
			throw new UserNotFoundException(id);
		}
	}
	
	public boolean existsSharedTrip(Long idUser1, Long idUser2) {
		return tripRepository.existsSharedTrip(idUser1, idUser2);
	}
	
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	

}
