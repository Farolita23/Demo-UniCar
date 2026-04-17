package com.daw.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.daw.controller.dto.WarningDTO;
import com.daw.service.WarningService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/warning")
@RequiredArgsConstructor
public class WarningController {

    private final WarningService warningService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WarningDTO>> getMyWarnings(@PathVariable Long userId) {
        return ResponseEntity.ok(warningService.getWarningsByUser(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<WarningDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(warningService.markAsRead(id));
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of("count", warningService.countUnread(userId)));
    }
}
