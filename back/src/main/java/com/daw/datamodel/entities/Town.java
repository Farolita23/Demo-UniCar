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

@Entity
@Table(name = "town")
@Getter
@Setter
public class Town {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "zip_code", nullable = false, unique = true)
    private String zipCode;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "town", fetch = FetchType.LAZY)
    private Set<Trip> trips;
    
    @OneToMany(mappedBy = "homeTown", fetch = FetchType.LAZY)
    private Set<User> users;

}
