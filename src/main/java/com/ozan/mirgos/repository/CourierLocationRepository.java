package com.ozan.mirgos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ozan.mirgos.entity.CourierLocation;

@Repository
public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {
    @Query("SELECT cl FROM CourierLocation cl WHERE cl.courierId = :courierId AND cl.id != :currentId AND cl.time < :currentTime ORDER BY cl.time DESC")
    List<CourierLocation> findPreviousLocationByCourierId(@Param("courierId") Long courierId, @Param("currentId") Long currentId, @Param("currentTime") LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT cl FROM CourierLocation cl WHERE cl.courierId = :courierId AND cl.time >= :time ORDER BY cl.time ASC")
    List<CourierLocation> findByCourierIdAndTime(@Param("courierId") Long courierId, @Param("time") LocalDateTime time);
}
