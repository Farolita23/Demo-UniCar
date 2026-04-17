package com.daw.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.WarningCreateDTO;
import com.daw.controller.dto.WarningDTO;
import com.daw.controller.dto.mapper.WarningMapper;
import com.daw.datamodel.entities.Warning;
import com.daw.datamodel.repository.WarningRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WarningService {

    private final WarningRepository warningRepository;
    private final WarningMapper warningMapper;
    private final GeneralService generalService;

    public List<WarningDTO> getWarningsByUser(Long userId) {
        return warningMapper.toListDto(warningRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    public WarningDTO create(WarningCreateDTO dto, Long adminId) {
        Warning w = new Warning();
        w.setSubject(dto.getSubject());
        w.setMessage(dto.getMessage());
        w.setCreatedAt(LocalDateTime.now());
        w.setIsRead(false);
        w.setUser(generalService.findUserById(dto.getIdUser()));
        w.setAdmin(generalService.findUserById(adminId));
        warningRepository.save(w);
        return warningMapper.toDto(w);
    }

    public WarningDTO markAsRead(Long warningId) {
        Warning w = warningRepository.findById(warningId)
                .orElseThrow(() -> new RuntimeException("Warning not found"));
        w.setIsRead(true);
        warningRepository.save(w);
        return warningMapper.toDto(w);
    }

    public long countUnread(Long userId) {
        return warningRepository.countByUserIdAndIsReadFalse(userId);
    }
}
