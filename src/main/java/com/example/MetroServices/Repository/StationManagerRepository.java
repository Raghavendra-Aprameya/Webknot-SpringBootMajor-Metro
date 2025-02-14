package com.example.MetroServices.Repository;

import com.example.MetroServices.Entity.StationEntity;
import com.example.MetroServices.Entity.StationManagerEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StationManagerRepository extends JpaRepository<StationManagerEntity,Long> {
//    @Query("SELECT s.contact FROM StationManagerEntity s WHERE s.stationId = :stationId")
//    Optional<String> getContactById(@Param("stationId") long stationId);

    String findByStation(StationEntity stationOpt);
}
