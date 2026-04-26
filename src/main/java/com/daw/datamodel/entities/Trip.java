package com.daw.datamodel.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad JPA que representa un viaje compartido publicado en la plataforma UniCar.
 *
 * Un viaje conecta una localidad ({@link Town}) con un campus ({@link Campus}) o
 * viceversa, dependiendo del sentido indicado por {@code isToCampus}. El conductor
 * es el propietario del vehículo asociado. El ciclo de vida de un pasajero en el
 * viaje pasa por tres estados: solicitante → pasajero confirmado (o rechazado).
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see Car
 * @see Campus
 * @see Town
 * @see User
 */
@Entity
@Table(name = "trip")
@Getter
@Setter
@ToString(exclude = {"passengers", "requesters"})
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_trip_car")
    )
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_trip_campus")
    )
    private Campus campus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_trip_town")
    )
    private Town town;

    /** {@code true} si el viaje va desde la localidad hacia el campus; {@code false} en sentido inverso. */
    @Column(name = "is_to_campus", nullable = false)
    private Boolean isToCampus;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "departure_address", nullable = false)
    private String departureAddress;

    @Column(name = "price", nullable = false, precision = 4, scale = 2)
    private BigDecimal price;

    /** Conjunto de pasajeros confirmados por el conductor. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "trip_passenger",
        joinColumns = @JoinColumn(name = "trip_id"),
        inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    private Set<User> passengers = new HashSet<>();

    /** Conjunto de usuarios que han solicitado una plaza y están pendientes de aprobación. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "trip_requester",
        joinColumns = @JoinColumn(name = "trip_id"),
        inverseJoinColumns = @JoinColumn(name = "requester_id")
    )
    private Set<User> requesters = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id != null && Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

}
