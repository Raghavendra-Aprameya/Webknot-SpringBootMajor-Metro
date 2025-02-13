package com.example.MetroServices.Entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "station_managers")
public class StationManagerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private StationEntity station;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Override
    public String toString() {
        return "StationManagerEntity{" +
                "id=" + id +
                ", station=" + station +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}