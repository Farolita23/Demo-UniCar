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

@Service
@Transactional
@RequiredArgsConstructor
public class RatingService {
	
	private final RatingRepository ratingRepository;
	private final RatingMapper ratingMapper;
	private final RatingCreateMapper ratingCreateMapper;
	private final RatingUpdateMapper ratingUpdateMapper;
	private final GeneralService generalService;
	
	public List<RatingDTO> findAll() {
		return ratingMapper.toListDto(ratingRepository.findAll());
	}

	public RatingDTO findById(Long id) {
		return ratingMapper.toDto(generalService.findRatingById(id));
	}
	
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
	
	public RatingDTO update(RatingUpdateDTO dto, Long id) {
		Rating rating = generalService.findRatingById(id);
		ratingUpdateMapper.updateEntityFromDto(dto, rating);
		Rating updatedRating = ratingRepository.save(rating);
		return ratingMapper.toDto(updatedRating);
	}
	
	public void delete(Long id) {
		ratingRepository.delete(generalService.findRatingById(id));
	}
	
}
