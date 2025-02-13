package com.example.MetroServices.Repository;

import com.example.MetroServices.Entity.FareEntity;
import com.example.MetroServices.Entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FareRepository extends JpaRepository<FareEntity, Long> {


    Optional<FareEntity> findByFromStationAndToStation(StationEntity sourceStation, StationEntity destinationStation);
}
