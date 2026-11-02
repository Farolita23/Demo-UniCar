package com.daw.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.controller.dto.TripDTO;
import com.daw.controller.dto.UserSummaryDTO;
import com.daw.datamodel.repository.RatingRepository;

import lombok.RequiredArgsConstructor;

/**
 * Completa el conductor de cada viaje con su valoración media y su número de valoraciones,
 * para mostrarlos en las tarjetas de viaje. Hace una sola consulta agregada por listado,
 * en lugar de cargar las valoraciones de cada conductor por separado.
 */
@Service
@RequiredArgsConstructor
public class DriverStatsService {

    private final RatingRepository ratingRepository;

    @Transactional(readOnly = true)
    public Page<TripDTO> enrich(Page<TripDTO> page) {
        enrich(page.getContent());
        return page;
    }

    @Transactional(readOnly = true)
    public TripDTO enrich(TripDTO trip) {
        if (trip != null) enrich(List.of(trip));
        return trip;
    }

    @Transactional(readOnly = true)
    public void enrich(Collection<TripDTO> trips) {
        Set<Long> driverIds = trips.stream()
                .map(TripDTO::getDriverDTO)
                .filter(Objects::nonNull)
                .map(UserSummaryDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (driverIds.isEmpty()) return;

        Map<Long, Object[]> stats = new HashMap<>();
        for (Object[] row : ratingRepository.statsForRatedUsers(driverIds)) {
            stats.put((Long) row[0], row);
        }
        for (TripDTO trip : trips) {
            UserSummaryDTO driver = trip.getDriverDTO();
            if (driver == null || driver.getId() == null) continue;
            Object[] row = stats.get(driver.getId());
            driver.setAvgRating(row == null ? null : ((Number) row[1]).doubleValue());
            driver.setRatingsCount(row == null ? 0L : ((Number) row[2]).longValue());
        }
    }
}
