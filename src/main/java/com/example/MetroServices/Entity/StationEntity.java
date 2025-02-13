package com.example.MetroServices.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stations")
public class StationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationName;
    private String location;
    private Boolean isActive;
    //station Manager one to one


}
