package com.daw.security;

import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación de {@link UserDetailsService} que carga los datos del usuario desde la base de datos.
 *
 * Es utilizada por Spring Security durante el proceso de autenticación para obtener
 * el usuario, su contraseña cifrada y su rol a partir del nombre de usuario. Los roles
 * se mapean automáticamente con el prefijo {@code ROLE_} requerido por Spring Security.
 *
 * @author Adam Gavira
 * @version 1.0.0

 * @see SecurityConfig
 * @see UserRepository
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Localiza al usuario por su nombre de usuario y construye el objeto {@link UserDetails}
     * con las credenciales y autoridades necesarias para Spring Security.
     *
     * @param username nombre de usuario con el que se desea autenticar
     * @return objeto {@link UserDetails} con las credenciales del usuario
     * @throws UsernameNotFoundException si no existe ningún usuario con el nombre indicado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
