package com.example.MetroServices.Entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "checkin_checkout")
public class CheckInOutEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ActiveUserEntity user;

    @ManyToOne
    @JoinColumn(name = "source_station")
    private StationEntity sourceStation;

    @ManyToOne
    @JoinColumn(name = "destination_station")
    private StationEntity destinationStation;

    @Column()
    private LocalDateTime checkInTime;

    @Column()
    private LocalDateTime checkOutTime;

    @Column()
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