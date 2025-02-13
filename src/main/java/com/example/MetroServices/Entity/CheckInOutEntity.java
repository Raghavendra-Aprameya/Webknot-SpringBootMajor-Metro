package com.example.MetroServices.Entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "checkin_checkout")
public class CheckInOutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ActiveUserEntity user;

    @ManyToOne
    @JoinColumn(name = "source_station", nullable = false)
    private StationEntity sourceStation;

    @ManyToOne
    @JoinColumn(name = "destination_station")
    private StationEntity destinationStation;

    @Column(nullable = false)
    private LocalDateTime checkInTime;

    @Column(nullable = false)
    private LocalDateTime checkOutTime;

    @Column(nullable = false)
    private Double fare;

    @Override
    public String toString() {
        return "CheckinCheckoutEntity{" +
                "id=" + id +
                ", user=" + user +
                ", sourceStation=" + sourceStation +
                ", destinationStation=" + destinationStation +
                ", checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                ", fare=" + fare +
                '}';
    }
}