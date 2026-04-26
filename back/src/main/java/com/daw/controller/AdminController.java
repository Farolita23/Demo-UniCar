package com.daw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.daw.controller.dto.ReportDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.service.ReportService;
import com.daw.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST que expone los endpoints exclusivos del rol de administrador.
 *
 * Proporciona operaciones de gestión de usuarios (listado, búsqueda, baneo,
 * desbaneo, strikes y eliminación) y de gestión de reportes (listado y eliminación).
 * Todos los endpoints de este controlador requieren el rol {@code ADMIN}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see UserService
 * @see ReportService
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ReportService reportService;

    /**
     * Recupera la lista completa de usuarios registrados en el sistema.
     *
     * @return {@code 200 OK} con la lista de {@link UserDTO}
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Busca usuarios cuyo nombre o nombre de usuario contenga el término proporcionado.
     *
     * @param q término de búsqueda parcial
     * @return {@code 200 OK} con la lista de {@link UserDTO} coincidentes
     */
    @GetMapping("/users/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }

    /**
     * Recupera todos los reportes registrados en el sistema.
     *
     * @return {@code 200 OK} con la lista de {@link ReportDTO}
     */
    @GetMapping("/reports")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.findAll());
    }

    /**
     * Banea a un usuario, impidiéndole acceder a la plataforma.
     *
     * @param id identificador del usuario a banear
     * @return {@code 200 OK} si la operación se completó correctamente
     */
    @PostMapping("/users/{id}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Long id) {
        userService.banUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Levanta el baneo de un usuario, restaurando su acceso a la plataforma.
     *
     * @param id identificador del usuario al que se levanta el baneo
     * @return {@code 200 OK} si la operación se completó correctamente
     */
    @PostMapping("/users/{id}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Añade un strike al usuario indicado. Al acumular tres strikes el usuario es baneado automáticamente.
     *
     * @param id identificador del usuario al que se añade el strike
     * @return {@code 200 OK} si la operación se completó correctamente
     */
    @PostMapping("/users/{id}/strike")
    public ResponseEntity<Void> addStrike(@PathVariable Long id) {
        userService.addStrike(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Elimina un usuario del sistema si no tiene dependencias activas.
     *
     * @param id identificador del usuario a eliminar
     * @return {@code 204 No Content} si la eliminación se realizó correctamente
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina un reporte del sistema.
     *
     * @param id identificador del reporte a eliminar
     * @return {@code 204 No Content} si la eliminación se realizó correctamente
     */
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
