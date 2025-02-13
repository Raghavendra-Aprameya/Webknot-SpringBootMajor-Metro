package com.example.MetroServices.Repository;

import com.example.MetroServices.Entity.CheckInOutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInOutRepository extends JpaRepository<CheckInOutEntity,Long> {
}
