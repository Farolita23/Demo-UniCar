package com.daw.datamodel.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa un campus universitario
 *
 * Los campus actúan como puntos de origen o destino de los viajes publicados.
 * Cada usuario tiene un campus habitual y cada viaje está asociado a exactamente
 * un campus. No se permite eliminar un campus que tenga usuarios o viajes activos.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see Trip
 * @see User
 */
@Entity
@Table(name = "campus")
@Getter
@Setter
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "campus", fetch = FetchType.LAZY)
    private Set<Trip> trips;

    @OneToMany(mappedBy = "usualCampus", fetch = FetchType.LAZY)
    private Set<User> users;

}
