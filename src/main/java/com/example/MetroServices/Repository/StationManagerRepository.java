package com.example.MetroServices.Repository;

import com.example.MetroServices.Entity.StationManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationManagerRepository extends JpaRepository<StationManagerEntity,Long> {
    String getContactById(long stationId);
}
