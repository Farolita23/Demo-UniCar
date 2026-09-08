package com.daw.datamodel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.PeriodicTrip;

/**
 * Repositorio de acceso a datos para la entidad {@link PeriodicTrip}.
 *
 * @author Javier Falcon
 * @version 1.1.0
 * @see PeriodicTrip
 */
@Repository
public interface PeriodicTripRepository extends JpaRepository<PeriodicTrip, Long> {

    /**
     * Recupera las plantillas periódicas cuyos vehículos pertenecen al conductor indicado,
     * ordenadas de la más reciente a la más antigua por fecha de inicio.
     *
     * @param driverId identificador del conductor
     * @param pageable configuración de paginación
     * @return página de plantillas periódicas del conductor
     */
    @Query("SELECT pt FROM PeriodicTrip pt WHERE pt.car.driver.id = :driverId " +
           "ORDER BY pt.startDate DESC")
    Page<PeriodicTrip> findByDriverId(@Param("driverId") Long driverId, Pageable pageable);
}
