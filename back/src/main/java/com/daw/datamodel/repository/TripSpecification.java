package com.daw.datamodel.repository;

import com.daw.controller.dto.TripSearchDTO;
import com.daw.datamodel.entities.Trip;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripSpecification {

    public static Specification<Trip> withFilters(TripSearchDTO filters) {
        return (Root<Trip> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Solo viajes futuros (hoy incluido)
            predicates.add(cb.greaterThanOrEqualTo(
                root.get("departureDate"), LocalDate.now()
            ));

            if (filters.getCampusId() != null) {
                predicates.add(cb.equal(
                    root.get("campus").get("id"), filters.getCampusId()
                ));
            }

            if (filters.getTownId() != null) {
                predicates.add(cb.equal(
                    root.get("town").get("id"), filters.getTownId()
                ));
            }

            if (filters.getIsToCampus() != null) {
                predicates.add(cb.equal(
                    root.get("isToCampus"), filters.getIsToCampus()
                ));
            }

            if (filters.getDepartureDate() != null) {
                predicates.add(cb.equal(
                    root.get("departureDate"), filters.getDepartureDate()
                ));
            }

            if (filters.getDepartureTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                    root.get("departureTime"), filters.getDepartureTime()
                ));
            }

            if (filters.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                    root.get("price"), filters.getMaxPrice()
                ));
            }

            // Plazas libres: solo se filtra si el usuario especifica minFreeSeats
            if (filters.getMinFreeSeats() != null && filters.getMinFreeSeats() > 0) {
                Expression<Integer> capacity = root.get("car").get("capacity");
                Expression<Integer> passengers = cb.size(root.get("passengers"));
                // Plazas libres = capacidad total - pasajeros aceptados
                // (no restamos conductor porque capacity ya es el nº de plazas)
                Expression<Integer> freeSeats = cb.diff(capacity, passengers);
                predicates.add(cb.greaterThanOrEqualTo(freeSeats, filters.getMinFreeSeats()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
