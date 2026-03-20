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

import com.daw.controller.dto.ReportCreateDTO;
import com.daw.controller.dto.ReportDTO;
import com.daw.controller.dto.ReportUpdateDTO;
import com.daw.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportDTO>> findAll() {
        return ResponseEntity.ok(reportService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReportDTO> create(@RequestBody @Validated ReportCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.create(dto));
    }

    /**
     * Solo se actualiza el motivo y la fecha del reporte (ReportUpdateDTO),
     * no los usuarios involucrados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> update(@RequestBody @Validated ReportUpdateDTO dto,
                                             @PathVariable Long id) {
        return ResponseEntity.ok(reportService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
