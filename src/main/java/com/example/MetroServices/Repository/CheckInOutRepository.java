package com.example.MetroServices.Repository;

import com.example.MetroServices.Entity.CheckInOutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckInOutRepository extends JpaRepository<CheckInOutEntity,Long> {
    List<CheckInOutEntity> findBySourceStationIsNotNullAndDestinationStationIsNull();

}
