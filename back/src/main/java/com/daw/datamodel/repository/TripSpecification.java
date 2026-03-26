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

            // Solo viajes futuros
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

            // isToCampus: solo se aplica si NO es null (null = mostrar todos)
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

            // Plazas libres: capacity - 1 (conductor) - passengers >= minFreeSeats
            int minSeats = filters.getMinFreeSeats() != null ? filters.getMinFreeSeats() : 1;
            Expression<Integer> capacity = root.get("car").get("capacity");
            Expression<Integer> passengers = cb.size(root.get("passengers"));
            Expression<Integer> freeSeats = cb.diff(cb.diff(capacity, 1), passengers);
            predicates.add(cb.greaterThanOrEqualTo(freeSeats, minSeats));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
