package com.daw.controller;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.controller.dto.UserLoginDTO;
import com.daw.security.JwtUtil;
import com.daw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * POST /api/auth/login
     * Body: { "username": "...", "password": "..." }
     * Devuelve: { "token": "eyJ..." }
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * POST /api/auth/register
     * Body: UserCreateDTO (mismos campos que tenías en /api/user)
     * Devuelve: UserDTO del usuario creado
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Validated UserCreateDTO dto) {
        UserDTO created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
