package com.daw.datamodel.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa una relación de favorito entre dos usuarios de la plataforma.
 *
 * Un usuario puede marcar a otros usuarios como favoritos para encontrarlos
 * con mayor facilidad al buscar viajes. La restricción de unicidad compuesta
 * ({@code user_id}, {@code favorite_user_id}) impide duplicados a nivel de base de datos.
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @see User
 */
@Entity
@Table(name = "favorite", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "favorite_user_id"})
})
@Getter
@Setter
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario que añade al favorito. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_favorite_user"))
    private User user;

    /** Usuario que ha sido marcado como favorito. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_favorite_target"))
    private User favoriteUser;
}
