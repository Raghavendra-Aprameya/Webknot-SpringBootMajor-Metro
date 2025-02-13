package com.example.MetroServices.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_outs")
public class CheckOutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    @OneToOne
    @JoinColumn(name = "check_in_id", referencedColumnName = "id")
    private CheckInEntity checkIn;

    @ManyToOne
    @JoinColumn(name = "exit_station_id", referencedColumnName = "id")
    private StationEntity exitStation;

    private LocalDateTime checkOutTime;
    private Double fareAmount;
    private Boolean paymentStatus = false;

    // Getters and Setters
}
