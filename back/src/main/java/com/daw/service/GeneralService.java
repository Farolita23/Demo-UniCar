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

/**
 * Servicio transversal que centraliza la recuperación de entidades del dominio con gestión de excepciones.
 *
 * Evita la duplicación de código de búsqueda con lanzamiento de excepción en los servicios
 * especializados. Cada método garantiza que la entidad existe o lanza la excepción de dominio
 * correspondiente, simplificando la lógica en las capas superiores.
 *
 * También expone métodos de validación de unicidad utilizados durante el registro
 * y actualización de usuarios.
 *
 * @author Adam y Javier
 * @version 1.0.0
 * @see CampusRepository
 * @see CarRepository
 * @see RatingRepository
 * @see ReportRepository
 * @see TownRepository
 * @see TripRepository
 * @see UserRepository
 */
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

	/**
	 * Recupera un campus por su identificador o lanza una excepción si no existe.
	 *
	 * @param id identificador del campus
	 * @return entidad {@link Campus} encontrada
	 * @throws CampusNotFoundException si no existe un campus con ese identificador
	 */
	public Campus findCampusById(Long id) {
		Optional<Campus> campus = campusRepository.findById(id);
		if(campus.isPresent()) {
			return campus.get();
		} else {
			throw new CampusNotFoundException(id);
		}
	}

	/**
	 * Recupera un vehículo por su identificador o lanza una excepción si no existe.
	 *
	 * @param id identificador del vehículo
	 * @return entidad {@link Car} encontrada
	 * @throws CarNotFoundException si no existe un vehículo con ese identificador
	 */
	public Car findCarById(Long id) {
		Optional<Car> car = carRepository.findById(id);
		if(car.isPresent()) {
			return car.get();
		} else {
			throw new CarNotFoundException(id);
		}
	}

	/**
	 * Recupera una valoración por su identificador o lanza una excepción si no existe.
	 *
	 * @param id identificador de la valoración
	 * @return entidad {@link Rating} encontrada
	 * @throws RatingNotFoundException si no existe una valoración con ese identificador
	 */
	public Rating findRatingById(Long id) {
		Optional<Rating> rating = ratingRepository.findById(id);
		if(rating.isPresent()) {
			return rating.get();
		} else {
			throw new RatingNotFoundException(id);
		}
	}

	/**
	 * Recupera un reporte por su identificador o lanza una excepción si no existe.
	 *
	 * @param id identificador del reporte
	 * @return entidad {@link Report} encontrada
	 * @throws ReportNotFoundException si no existe un reporte con ese identificador
	 */
	public Report findReportById(Long id) {
		Optional<Report> report = reportRepository.findById(id);
		if(report.isPresent()) {
			return report.get();
		} else {
			throw new ReportNotFoundException(id);
		}
	}

	/**
	 * Recupera una localidad por su identificador o lanza una excepción si no existe.
	 *
	 * @param id identificador de la localidad
	 * @return entidad {@link Town} encontrada
	 * @throws TownNotFoundException si no existe una localidad con ese identificador
	 */
	public Town findTownById(Long id) {
		Optional<Town> town = townRepository.findById(id);
		if(town.isPresent()) {
			return town.get();
		} else {
			throw new TownNotFoundException(id);
		}
	}

	/**
	 * Recupera un viaje por su identificador o lanza una excepción si no existe.
	 *
	 * @param id identificador del viaje
	 * @return entidad {@link Trip} encontrada
	 * @throws TripNotFoundException si no existe un viaje con ese identificador
	 */
	public Trip findTripById(Long id) {
		Optional<Trip> trip = tripRepository.findById(id);
		if(trip.isPresent()) {
			return trip.get();
		} else {
			throw new TripNotFoundException(id);
		}
	}

	/**
	 * Recupera un usuario por su identificador o lanza una excepción si no existe.
	 *
	 * @param id identificador del usuario
	 * @return entidad {@link User} encontrada
	 * @throws UserNotFoundException si no existe un usuario con ese identificador
	 */
	public User findUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return user.get();
		} else {
			throw new UserNotFoundException(id);
		}
	}

	/**
	 * Verifica si dos usuarios han compartido al menos un viaje juntos.
	 *
	 * @param idUser1 identificador del primer usuario
	 * @param idUser2 identificador del segundo usuario
	 * @return {@code true} si comparten historial de viaje, {@code false} en caso contrario
	 */
	public boolean existsSharedTrip(Long idUser1, Long idUser2) {
		return tripRepository.existsSharedTrip(idUser1, idUser2);
	}

	/**
	 * Comprueba si un nombre de usuario ya está registrado en el sistema.
	 *
	 * @param username nombre de usuario a verificar
	 * @return {@code true} si ya existe, {@code false} en caso contrario
	 */
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	/**
	 * Comprueba si una dirección de correo electrónico ya está registrada en el sistema.
	 *
	 * @param email dirección de correo a verificar
	 * @return {@code true} si ya existe, {@code false} en caso contrario
	 */
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

}
