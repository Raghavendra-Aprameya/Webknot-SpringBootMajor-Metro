package com.example.MetroServices.Repository;

import com.example.MetroServices.Entity.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckInEntity,Long> {
}
