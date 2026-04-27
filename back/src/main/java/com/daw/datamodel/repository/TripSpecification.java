package com.daw.datamodel.repository;

import com.daw.controller.dto.TripSearchDTO;
import com.daw.datamodel.entities.Trip;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Especificación JPA para la búsqueda dinámica de viajes con filtros opcionales.
 *
 * Construye predicados Criteria API a partir de los campos no nulos del
 * {@link TripSearchDTO}, lo que evita el problema conocido de Hibernate con
 * operaciones {@code IS NULL} sobre campos {@code Boolean} en JPQL estático.
 * Siempre se aplica el predicado base que restringe los resultados a viajes
 * con fecha de salida igual o posterior al día actual.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see TripRepository
 * @see TripSearchDTO
 */
public class TripSpecification {

    /**
     * Genera una {@link Specification} de viajes aplicando solo los filtros presentes en el DTO.
     *
     * @param filters objeto con los criterios de búsqueda; los campos nulos se ignoran
     * @return especificación combinada lista para ejecutar sobre {@link TripRepository}
     */
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

            if (filters.getMinFreeSeats() != null && filters.getMinFreeSeats() > 0) {
                Expression<Integer> capacity = root.get("car").get("capacity");
                Expression<Integer> passengers = cb.size(root.get("passengers"));
                // Plazas libres = capacidad total − pasajeros confirmados
                Expression<Integer> freeSeats = cb.diff(capacity, passengers);
                predicates.add(cb.greaterThanOrEqualTo(freeSeats, filters.getMinFreeSeats()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
