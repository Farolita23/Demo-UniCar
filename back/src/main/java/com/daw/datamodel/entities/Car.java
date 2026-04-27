package com.daw.datamodel.entities;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad JPA que representa un vehículo registrado en la plataforma UniCar.
 *
 * Cada vehículo pertenece a un conductor ({@link User}) y puede estar asociado
 * a múltiples viajes. La capacidad del vehículo determina el número máximo de
 * plazas disponibles en cada viaje. No se permite eliminar un vehículo que
 * tenga viajes asociados.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see User
 * @see Trip
 */
@Entity
@Table(name = "car")
@Getter
@Setter
@ToString(exclude = {"driver", "trips"})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_car_user")
    )
    private User driver;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name ="color", nullable = false)
    private String color;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private Set<Trip> trips;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id != null && Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

}
