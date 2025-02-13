package com.example.MetroServices.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data

public class CheckInDTO {
    private Long userId;
    private Long stationId;

    @Override
    public String toString() {
        return "CheckInDTO{" +
                "userId=" + userId +
                ", stationId=" + stationId +
                '}';
    }
}

