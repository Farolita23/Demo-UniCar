package com.daw.controller;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.controller.dto.UserLoginDTO;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;
import com.daw.security.JwtUtil;
import com.daw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST que gestiona los endpoints de autenticación y registro de usuarios.
 *
 * Proporciona tres operaciones principales: inicio de sesión con generación de token JWT,
 * registro de nuevos usuarios y consulta del perfil del usuario autenticado. Los endpoints
 * de login y registro son públicos; {@code /me} requiere autenticación válida.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see JwtUtil
 * @see UserService
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Autentica al usuario con sus credenciales y devuelve un token JWT si son válidas.
     *
     * @param dto credenciales de inicio de sesión (nombre de usuario y contraseña)
     * @return {@code 200 OK} con un mapa {@code {"token": "<jwt>"}}
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(auth.getName(), user.getRole())));
    }

    /**
     * Registra un nuevo usuario en la plataforma y devuelve sus datos creados.
     *
     * @param dto datos del nuevo usuario, validados mediante {@code @Validated}
     * @return {@code 201 Created} con el {@link UserDTO} del usuario recién registrado
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Validated UserCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    /**
     * Devuelve el perfil del usuario autenticado extraído del token JWT en curso.
     *
     * @param authentication objeto de autenticación inyectado por Spring Security
     * @return {@code 200 OK} con el {@link UserDTO} del usuario autenticado,
     *         o {@code 401 Unauthorized} si la autenticación no es válida
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.findByUsername(authentication.getName()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
