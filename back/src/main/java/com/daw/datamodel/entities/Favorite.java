package com.daw.datamodel.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_favorite_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_favorite_target"))
    private User favoriteUser;
}
