package com.daw.datamodel.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa una valoración realizada por un usuario a otro.
 *
 * Las valoraciones se emiten sobre una escala de 1 a 5 y solo son posibles
 * entre usuarios que han compartido al menos un viaje. Cada par de usuarios
 * (emisor, receptor) puede generar únicamente una valoración, garantizando
 * así la imparcialidad del sistema de reputación
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see User
 */
@Entity
@Table(name = "rating")
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Puntuación otorgada, entre 1 y 5 inclusive. */
    @Column(name = "rating", nullable = false)
    private Integer rating;

    /** Usuario que emite la valoración. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_rate_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_rating_user_rate")
    )
    private User userRate;

    /** Usuario que recibe la valoración. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rated_user_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_rating_rated_user")
    )
    private User ratedUser;

}
