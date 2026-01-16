package com.daw.datamodel.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "phone", nullable = false, length = 9)
    private String phone;

    @Column(name = "strikes", nullable = false)
    private Integer strikes;

    @Column(name = "banned", nullable = false)
    private Boolean banned;

    @Column(name = "driving_license_year", nullable = true)
    private Integer drivingLicenseYear;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usual_campus_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_campus")
    )
    private Campus usualCampus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_town_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_user_town")
    )
    private Town homeTown;

    @OneToMany(mappedBy = "reportedUser", fetch = FetchType.LAZY)
    private Set<Report> reportsDone;
    
    @OneToMany(mappedBy = "userReport", fetch = FetchType.LAZY)
    private Set<Report> reportsReceived;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Car> cars;

    @Column(name = "description", nullable = true)
    private String description;

    @ManyToMany(mappedBy = "passengers", fetch = FetchType.LAZY)
    private Set<Trip> tripsAsAPassenger;

    @OneToMany(mappedBy = "ratedUser", fetch = FetchType.LAZY)
    private Set<Rating> ratingsReceived;

    @OneToMany(mappedBy = "userRate", fetch = FetchType.LAZY)
    private Set<Rating> ratingsDone;

}


