package com.ozan.mirgos.repository;

import com.ozan.mirgos.entity.CourierLocation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {
    
    @Query("SELECT cl FROM CourierLocation cl WHERE cl.courierId = :courierId AND cl.id != :currentId ORDER BY cl.time DESC")
    List<CourierLocation> findPreviousLocationByCourierId(@Param("courierId") Long courierId, @Param("currentId") Long currentId, Pageable pageable);
}
