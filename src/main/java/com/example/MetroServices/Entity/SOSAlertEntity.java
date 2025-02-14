package com.example.MetroServices.Entity;



import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@Table(name = "sos_alerts")
public class SOSAlertEntity  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private StationEntity station;

    @Column(nullable = false)
    private boolean resolved;

    @Override
    public String toString() {
        return "SOSAlertEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", station=" + station +
                ", resolved=" + resolved +
                '}';
    }
}