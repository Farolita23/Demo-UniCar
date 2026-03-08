package com.daw.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody @Validated UserCreateDTO dto,
                                          @PathVariable Long id) {
        return ResponseEntity.ok(userService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Obtiene el dueño de un coche específico
     */
    @GetMapping("/car-owner/{idCar}")
    public ResponseEntity<UserDTO> getCarOwner(@PathVariable Long idCar) {
        return ResponseEntity.ok(userService.getCarOwner(idCar));
    }

    /**
     * Obtiene el autor de una valoración
     */
    @GetMapping("/rating-author/{idRating}")
    public ResponseEntity<UserDTO> getRatingAuthor(@PathVariable Long idRating) {
        return ResponseEntity.ok(userService.getRatingAuthor(idRating) );
    }

    /**
     * Obtiene el usuario que fue valorado
     */
    @GetMapping("/rated-user/{idRating}")
    public ResponseEntity<UserDTO> getRatedUser(@PathVariable Long idRating) {
        return ResponseEntity.ok(userService.getRatedUser(idRating));
    }

    /**
     * Obtiene el autor de un reporte
     */
    @GetMapping("/report-author/{idReport}")
    public ResponseEntity<UserDTO> getReportAuthor(@PathVariable Long idReport) {
        return ResponseEntity.ok(userService.getReportAuthor(idReport));
    }

    /**
     * Obtiene el usuario que ha sido reportado
     */
    @GetMapping("/reported-user/{idReport}")
    public ResponseEntity<UserDTO> getReportedUser(@PathVariable Long idReport) {
        return ResponseEntity.ok(userService.getReportedUser(idReport));
    }
}