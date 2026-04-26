package com.daw.datamodel.entities;

import java.time.LocalDate;

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
 * Entidad JPA que representa un reporte de comportamiento entre usuarios de la plataforma.
 *
 * Los reportes permiten a los usuarios denunciar conductas inapropiadas a la
 * administración. Solo pueden emitirse entre usuarios que hayan compartido un viaje
 * y no está permitido auto-reportarse. Los administradores son los únicos que pueden
 * consultar y gestionar los reportes recibidos
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @see User
 */
@Entity
@Table(name = "report")
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    /** Usuario que ha sido reportado. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_reported_user")
    )
    private User reportedUser;

    /** Usuario que emite el reporte. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_report_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_report")
    )
    private User userReport;

    @Column(name = "date", nullable = false)
    private LocalDate date;

}
