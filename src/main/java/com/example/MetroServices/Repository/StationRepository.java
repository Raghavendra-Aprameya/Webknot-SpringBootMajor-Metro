package com.example.MetroServices.Repository;

import com.example.MetroServices.Entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<StationEntity,Long> {
    Optional<StationEntity> findByName(String destinationStationName);

    List<StationEntity> findByActiveTrue();
}
