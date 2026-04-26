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

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con los usuarios de la plataforma.
 *
 * Proporciona métodos para el registro, consulta, actualización y eliminación de usuarios,
 * así como operaciones administrativas como el baneo, desbaneo y asignación de strikes.
 * Garantiza la unicidad de nombre de usuario y correo electrónico, y cifra la contraseña
 * antes de persistirla.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see UserRepository
 * @see UserMapper
 */
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

    /**
     * Recupera todos los usuarios registrados en el sistema.
     *
     * @return lista de {@link UserDTO} con todos los usuarios disponibles
     */
    public List<UserDTO> findAll() {
        return userMapper.toListDto(userRepository.findAll());
    }

    /**
     * Recupera un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return {@link UserDTO} con los datos del usuario encontrado
     * @throws UserNotFoundException si no existe un usuario con ese identificador
     */
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    /**
     * Recupera un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return {@link UserDTO} con los datos del usuario encontrado
     * @throws UserNotFoundException si no existe un usuario con ese nombre de usuario
     */
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(0L));
        return userMapper.toDto(user);
    }

    /**
     * Registra un nuevo usuario en la plataforma cifrando su contraseña y verificando unicidad de credenciales.
     *
     * @param dto datos del usuario a registrar
     * @return {@link UserDTO} con los datos del usuario recién creado
     * @throws DuplicateUsernameException si el nombre de usuario ya está en uso
     * @throws DuplicateEmailException    si el correo electrónico ya está registrado
     */
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

    /**
     * Actualiza los datos de perfil de un usuario existente.
     *
     * Si se envía una nueva contraseña, se cifra antes de persistirla.
     * El campus y la localidad habitual solo se actualizan si se proporcionan en el DTO.
     *
     * @param dto datos actualizados del usuario
     * @param id  identificador del usuario a modificar
     * @return {@link UserDTO} con los datos del usuario actualizado
     * @throws UserNotFoundException      si no existe un usuario con ese identificador
     * @throws DuplicateUsernameException si el nuevo nombre de usuario ya está en uso por otro usuario
     * @throws DuplicateEmailException    si el nuevo correo electrónico ya está registrado por otro usuario
     */
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

    /**
     * Elimina un usuario si no tiene vehículos registrados ni viajes activos como pasajero.
     *
     * @param id identificador del usuario a eliminar
     * @throws UserNotFoundException           si no existe un usuario con ese identificador
     * @throws EntityWithDependenciesException si el usuario tiene vehículos o viajes como pasajero vinculados
     */
    public void delete(Long id) {
        User user = generalService.findUserById(id);
        boolean hasntCars             = user.getCars().isEmpty();
        boolean hasntTripsAsPassenger = user.getTripsAsAPassenger().isEmpty();
        if (!(hasntCars && hasntTripsAsPassenger))
            throw new EntityWithDependenciesException("User", id);
        userRepository.delete(user);
    }

    /**
     * Recupera el propietario de un vehículo a partir del identificador del vehículo.
     *
     * @param idCar identificador del vehículo
     * @return {@link UserDTO} con los datos del propietario del vehículo
     */
    public UserDTO getCarOwner(Long idCar) {
        generalService.findCarById(idCar);
        return userMapper.toDto(userRepository.findByCarsId(idCar));
    }

    /**
     * Recupera el usuario que emitió una valoración concreta.
     *
     * @param idRating identificador de la valoración
     * @return {@link UserDTO} con los datos del autor de la valoración
     */
    public UserDTO getRatingAuthor(Long idRating) {
        generalService.findRatingById(idRating);
        return userMapper.toDto(userRepository.findByRatingsDoneId(idRating));
    }

    /**
     * Recupera el usuario que recibió una valoración concreta.
     *
     * @param idRating identificador de la valoración
     * @return {@link UserDTO} con los datos del usuario valorado
     */
    public UserDTO getRatedUser(Long idRating) {
        generalService.findRatingById(idRating);
        return userMapper.toDto(userRepository.findByRatingsReceivedId(idRating));
    }

    /**
     * Recupera el usuario que emitió un reporte concreto.
     *
     * @param idReport identificador del reporte
     * @return {@link UserDTO} con los datos del autor del reporte
     */
    public UserDTO getReportAuthor(Long idReport) {
        generalService.findReportById(idReport);
        return userMapper.toDto(userRepository.findByReportsDoneId(idReport));
    }

    /**
     * Recupera el usuario que ha sido reportado en un reporte concreto.
     *
     * @param idReport identificador del reporte
     * @return {@link UserDTO} con los datos del usuario reportado
     */
    public UserDTO getReportedUser(Long idReport) {
        generalService.findReportById(idReport);
        return userMapper.toDto(userRepository.findByReportsReceivedId(idReport));
    }

    /**
     * Busca usuarios cuyo nombre o nombre de usuario contenga el término proporcionado.
     *
     * @param query término de búsqueda (parcial, sin distinción de mayúsculas/minúsculas)
     * @return lista de {@link UserDTO} coincidentes; vacía si no hay resultados
     */
    public List<UserDTO> searchUsers(String query) {
        return userMapper.toListDto(userRepository.searchByNameOrUsername(query));
    }

    /**
     * Banea a un usuario, impidiéndole acceder a la plataforma.
     *
     * @param id identificador del usuario a banear
     * @throws UserNotFoundException si no existe un usuario con ese identificador
     */
    public void banUser(Long id) {
        User user = generalService.findUserById(id);
        user.setBanned(true);
        userRepository.save(user);
    }

    /**
     * Levanta el baneo de un usuario, restaurando su acceso a la plataforma.
     *
     * @param id identificador del usuario al que se levanta el baneo
     * @throws UserNotFoundException si no existe un usuario con ese identificador
     */
    public void unbanUser(Long id) {
        User user = generalService.findUserById(id);
        user.setBanned(false);
        userRepository.save(user);
    }

    /**
     * Añade un strike al contador de infracciones del usuario y lo banea automáticamente si alcanza 3.
     *
     * @param id identificador del usuario al que se añade el strike
     * @throws UserNotFoundException si no existe un usuario con ese identificador
     */
    public void addStrike(Long id) {
        User user = generalService.findUserById(id);
        user.setStrikes(user.getStrikes() + 1);
        if (user.getStrikes() >= 3) {
            user.setBanned(true);
        }
        userRepository.save(user);
    }
}
