package com.ozan.mirgos.repository;

import com.ozan.mirgos.entity.CourierLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {
}
