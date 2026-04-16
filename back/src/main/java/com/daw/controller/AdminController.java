package com.daw.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.daw.controller.dto.ReportDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.controller.dto.WarningCreateDTO;
import com.daw.controller.dto.WarningDTO;
import com.daw.datamodel.repository.UserRepository;
import com.daw.service.ReportService;
import com.daw.service.UserService;
import com.daw.service.WarningService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ReportService reportService;
    private final WarningService warningService;
    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.findAll());
    }

    @PostMapping("/users/{id}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Long id) {
        userService.banUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/strike")
    public ResponseEntity<Void> addStrike(@PathVariable Long id) {
        userService.addStrike(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/warnings")
    public ResponseEntity<WarningDTO> sendWarning(@RequestBody @Validated WarningCreateDTO dto,
                                                   Authentication auth) {
        Long adminId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found")).getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(warningService.create(dto, adminId));
    }

    @GetMapping("/warnings/user/{userId}")
    public ResponseEntity<List<WarningDTO>> getWarningsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(warningService.getWarningsByUser(userId));
    }
}
