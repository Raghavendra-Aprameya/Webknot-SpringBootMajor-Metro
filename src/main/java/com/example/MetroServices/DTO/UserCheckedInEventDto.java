package com.example.MetroServices.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data

public class UserCheckedInEventDto {
    private Long id;
    private Long userId;
    private Long stationId;
    private LocalDateTime checkInTime;
    private Boolean isCheckedOut;
}
