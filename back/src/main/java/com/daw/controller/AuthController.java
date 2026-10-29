package com.daw.controller;

import com.daw.controller.dto.GoogleLoginDTO;
import com.daw.controller.dto.GoogleRegisterDTO;
import com.daw.controller.dto.UserCreateDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.controller.dto.UserLoginDTO;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;
import com.daw.exceptions.UserIsBannedException;
import com.daw.security.GoogleTokenVerifier;
import com.daw.security.GoogleTokenVerifier.GoogleUser;
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

import java.util.LinkedHashMap;
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
    private final GoogleTokenVerifier googleTokenVerifier;

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
        if(user.getBanned()) {
            throw new UserIsBannedException("User is banned");
        }
        return ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(auth.getName(), user.getRole())));
    }

    /**
     * Inicia sesión con una cuenta de Google.
     *
     * Si el correo de Google ya pertenece a un usuario, devuelve su token JWT. Si no, responde
     * {@code {"needsRegistration": true, email, name, picture}} para que el cliente complete el
     * perfil y llame a {@code /google/register}.
     *
     * @param dto ID token emitido por Google Identity Services
     * @return {@code 200 OK} con {@code {"token": "<jwt>"}} o los datos de pre-registro
     */
    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> google(@RequestBody @Validated GoogleLoginDTO dto) {
        GoogleUser google = googleTokenVerifier.verify(dto.getIdToken());
        return userRepository.findByEmail(google.email())
            .map(user -> ResponseEntity.ok(tokenFor(user)))
            .orElseGet(() -> {
                Map<String, Object> pending = new LinkedHashMap<>();
                pending.put("needsRegistration", true);
                pending.put("email", google.email());
                pending.put("name", google.name());
                pending.put("picture", google.picture());
                return ResponseEntity.ok(pending);
            });
    }

    /**
     * Completa el registro de un usuario nuevo autenticado con Google y devuelve su token JWT.
     * El correo y el nombre se toman del ID token verificado; la contraseña queda aleatoria.
     *
     * @param dto ID token y datos de perfil que Google no proporciona
     * @return {@code 201 Created} con {@code {"token": "<jwt>"}}
     */
    @PostMapping("/google/register")
    public ResponseEntity<Map<String, Object>> googleRegister(@RequestBody @Validated GoogleRegisterDTO dto) {
        GoogleUser google = googleTokenVerifier.verify(dto.getIdToken());
        userService.createFromGoogle(dto, google);
        User user = userRepository.findByEmail(google.email())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenFor(user));
    }

    private Map<String, Object> tokenFor(User user) {
        if (user.getBanned()) {
            throw new UserIsBannedException("User is banned");
        }
        return Map.of("token", jwtUtil.generateToken(user.getUsername(), user.getRole()));
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
