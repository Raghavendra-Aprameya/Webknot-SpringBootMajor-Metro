package com.example.MetroServices.Entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "fares")
public class FareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_station", nullable = false)
    private StationEntity fromStation;

    @ManyToOne
    @JoinColumn(name = "to_station", nullable = false)
    private StationEntity toStation;

    @Column(nullable = false)
    private Double fareAmount;

    @Override
    public String toString() {
        return "FareEntity{" +
                "id=" + id +
                ", fromStation=" + fromStation +
                ", toStation=" + toStation +
                ", fareAmount=" + fareAmount +
                '}';
    }
}