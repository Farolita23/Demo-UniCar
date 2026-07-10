package com.daw.datamodel.entities;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Plantilla de configuración para una serie de viajes periódicos.
 *
 * Al persistirse, el servicio genera un {@link Trip} por cada fecha resultante
 * de combinar {@code startDate}, {@code endDate}, {@code daysOfWeek} y
 * {@code repeatIntervalWeeks}. Cada Trip generado mantiene una FK nullable
 * hacia esta entidad para trazabilidad.
 *
 * Los pasajeros y solicitantes se gestionan en cada Trip individual,
 * no en esta plantilla.
 */
@Entity
@Table(name = "periodic_trip")
@Getter
@Setter
@ToString
public class PeriodicTrip {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_periodic_trip_car")
    )
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_periodic_trip_campus")
    )
    private Campus campus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_periodic_trip_town")
    )
    private Town town;

    /** {@code true} si el viaje va desde la localidad hacia el campus; {@code false} en sentido inverso. */
    @Column(name = "is_to_campus", nullable = false)
    private Boolean isToCampus;


    @Column(name = "departure_address", nullable = false)
    private String departureAddress;

    @Column(name = "price", nullable = false, precision = 4, scale = 2)
    private BigDecimal price;

    /** Fecha de inicio obligatoria para la serie periódica (primer viaje). */
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Fecha límite obligatoria hasta la que se repite el viaje (inclusive). */
    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /** Hora de salida única para todos los días de la serie. */
    @NotNull
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    /** Días de la semana en los que se realiza el viaje (ej: LUNES, MIÉRCOLES). Obligatorio. */
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "periodic_trip_days", joinColumns = @JoinColumn(name = "periodic_trip_id"))
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek = EnumSet.noneOf(DayOfWeek.class);

    /** Repetir cada N semanas (1 = cada semana). */
    @NotNull
    @Min(1)
    @Column(name = "repeat_interval_weeks", nullable = false)
    private Integer repeatIntervalWeeks = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodicTrip trip = (PeriodicTrip) o;
        return id != null && Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }


}
