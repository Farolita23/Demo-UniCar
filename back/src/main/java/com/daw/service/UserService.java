package com.daw.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.controller.dto.UserUpdateDTO;
import com.daw.controller.dto.mapper.UserCreateMapper;
import com.daw.controller.dto.mapper.UserMapper;
import com.daw.controller.dto.mapper.UserUpdateMapper;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;
import com.daw.exceptions.DuplicateEmailException;
import com.daw.exceptions.DuplicateUsernameException;
import com.daw.exceptions.EntityWithDependenciesException;
import com.daw.exceptions.UserNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final GeneralService generalService;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        return userMapper.toListDto(userRepository.findAll());
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(0L));
        return userMapper.toDto(user);
    }

    public UserDTO create(UserCreateDTO dto) {
        if (generalService.existsByUsername(dto.getUsername())) throw new DuplicateUsernameException();
        if (generalService.existsByEmail(dto.getEmail()))       throw new DuplicateEmailException();

        User user = userCreateMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsualCampus(generalService.findCampusById(dto.getIdUsualCampus()));
        user.setHomeTown(generalService.findTownById(dto.getIdHomeTown()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDTO update(UserUpdateDTO dto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getUsername().equals(dto.getUsername()) && generalService.existsByUsername(dto.getUsername()))
            throw new DuplicateUsernameException();
        if (!user.getEmail().equals(dto.getEmail()) && generalService.existsByEmail(dto.getEmail()))
            throw new DuplicateEmailException();

        userUpdateMapper.updateEntityFromDto(dto, user);

        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getIdUsualCampus() != null)
            user.setUsualCampus(generalService.findCampusById(dto.getIdUsualCampus()));
        if (dto.getIdHomeTown() != null)
            user.setHomeTown(generalService.findTownById(dto.getIdHomeTown()));

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void delete(Long id) {
        User user = generalService.findUserById(id);
        boolean hasntCars              = user.getCars().isEmpty();
        boolean hasntTripsAsPassenger  = user.getTripsAsAPassenger().isEmpty();
        if (!(hasntCars && hasntTripsAsPassenger))
            throw new EntityWithDependenciesException("User", id);
        userRepository.delete(user);
    }

    public UserDTO getCarOwner(Long idCar) {
        generalService.findCarById(idCar);
        return userMapper.toDto(userRepository.findByCarsId(idCar));
    }

    public UserDTO getRatingAuthor(Long idRating) {
        generalService.findRatingById(idRating);
        return userMapper.toDto(userRepository.findByRatingsDoneId(idRating));
    }

    public UserDTO getRatedUser(Long idRating) {
        generalService.findRatingById(idRating);
        return userMapper.toDto(userRepository.findByRatingsReceivedId(idRating));
    }

    public UserDTO getReportAuthor(Long idReport) {
        generalService.findReportById(idReport);
        return userMapper.toDto(userRepository.findByReportsDoneId(idReport));
    }

    public UserDTO getReportedUser(Long idReport) {
        generalService.findReportById(idReport);
        return userMapper.toDto(userRepository.findByReportsReceivedId(idReport));
    }

    public List<UserDTO> searchUsers(String query) {
        return userMapper.toListDto(userRepository.searchByNameOrUsername(query));
    }

    public void banUser(Long id) {
        User user = generalService.findUserById(id);
        user.setBanned(true);
        userRepository.save(user);
    }

    public void unbanUser(Long id) {
        User user = generalService.findUserById(id);
        user.setBanned(false);
        userRepository.save(user);
    }

    public void addStrike(Long id) {
        User user = generalService.findUserById(id);
        user.setStrikes(user.getStrikes() + 1);
        if (user.getStrikes() >= 3) {
            user.setBanned(true);
        }
        userRepository.save(user);
    }
}
