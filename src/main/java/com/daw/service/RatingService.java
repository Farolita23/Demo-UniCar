package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.RatingCreateDTO;
import com.daw.controller.dto.RatingDTO;
import com.daw.controller.dto.RatingUpdateDTO;
import com.daw.controller.dto.mapper.RatingCreateMapper;
import com.daw.controller.dto.mapper.RatingMapper;
import com.daw.controller.dto.mapper.RatingUpdateMapper;
import com.daw.datamodel.entities.Rating;
import com.daw.datamodel.repository.RatingRepository;
import com.daw.exceptions.InvalidRatingException;
import com.daw.exceptions.RatingAlreadyExistsException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con las valoraciones de usuarios.
 *
 * Controla el ciclo de vida de las valoraciones aplicando las reglas del dominio:
 * un usuario solo puede valorar a otro si han compartido un viaje, no puede valorarse
 * a sí mismo y solo se permite una valoración por par de usuarios.
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @see RatingRepository
 * @see RatingMapper
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RatingService {

	private final RatingRepository ratingRepository;
	private final RatingMapper ratingMapper;
	private final RatingCreateMapper ratingCreateMapper;
	private final RatingUpdateMapper ratingUpdateMapper;
	private final GeneralService generalService;

	/**
	 * Recupera todas las valoraciones registradas en el sistema.
	 *
	 * @return lista de {@link RatingDTO} con todas las valoraciones disponibles
	 */
	public List<RatingDTO> findAll() {
		return ratingMapper.toListDto(ratingRepository.findAll());
	}

	/**
	 * Recupera una valoración por su identificador.
	 *
	 * @param id identificador de la valoración
	 * @return {@link RatingDTO} con los datos de la valoración encontrada
	 * @throws com.daw.exceptions.RatingNotFoundException si no existe una valoración con ese identificador
	 */
	public RatingDTO findById(Long id) {
		return ratingMapper.toDto(generalService.findRatingById(id));
	}

	/**
	 * Crea una nueva valoración verificando las reglas de negocio del dominio.
	 *
	 * @param dto datos de la valoración a crear
	 * @return {@link RatingDTO} con los datos de la valoración recién creada
	 * @throws InvalidRatingException        si el emisor intenta valorarse a sí mismo o no comparte viaje con el receptor
	 * @throws RatingAlreadyExistsException  si ya existe una valoración previa del mismo emisor al mismo receptor
	 */
	public RatingDTO create(RatingCreateDTO dto) {
		Long idUserRate = dto.getIdUserRate();
		Long idRatedUser = dto.getIdRatedUser();

		if(idUserRate.equals(idRatedUser) || !generalService.existsSharedTrip(idUserRate, idRatedUser)) {
			throw new InvalidRatingException(idUserRate, idRatedUser);
		}

		if(ratingRepository.existsByUserRateIdAndRatedUserId(idUserRate, idRatedUser)) {
			throw new RatingAlreadyExistsException(idUserRate, idRatedUser);
		}

		Rating rating = ratingCreateMapper.toEntity(dto);
		rating.setRatedUser(generalService.findUserById(idRatedUser));
		rating.setUserRate(generalService.findUserById(idUserRate));
		ratingRepository.save(rating);
		return ratingMapper.toDto(rating);
	}

	/**
	 * Actualiza la puntuación de una valoración existente.
	 *
	 * Solo se puede modificar la puntuación numérica; los usuarios implicados
	 * son inmutables una vez creada la valoración.
	 *
	 * @param dto datos actualizados de la valoración
	 * @param id  identificador de la valoración a modificar
	 * @return {@link RatingDTO} con los datos de la valoración actualizada
	 * @throws com.daw.exceptions.RatingNotFoundException si no existe una valoración con ese identificador
	 */
	public RatingDTO update(RatingUpdateDTO dto, Long id) {
		Rating rating = generalService.findRatingById(id);
		ratingUpdateMapper.updateEntityFromDto(dto, rating);
		Rating updatedRating = ratingRepository.save(rating);
		return ratingMapper.toDto(updatedRating);
	}

	/**
	 * Elimina una valoración por su identificador.
	 *
	 * @param id identificador de la valoración a eliminar
	 * @throws com.daw.exceptions.RatingNotFoundException si no existe una valoración con ese identificador
	 */
	public void delete(Long id) {
		ratingRepository.delete(generalService.findRatingById(id));
	}

}
