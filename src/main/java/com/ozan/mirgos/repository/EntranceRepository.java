package com.ozan.mirgos.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ozan.mirgos.entity.Entrance;

@Repository
public interface EntranceRepository extends JpaRepository<Entrance, Long> {
    
    @Query("SELECT e FROM Entrance e WHERE e.courierId = :courierId AND e.storeId = :storeId " +
           "AND e.eventTime >= :since")
    Optional<Entrance> findRecentEntrance(
            @Param("courierId") Long courierId,
            @Param("storeId") Long storeId,
            @Param("since") LocalDateTime since
    );
    
    @Query("SELECT e FROM Entrance e WHERE e.courierId = :courierId AND e.eventTime >= :since ORDER BY e.eventTime DESC")
    List<Entrance> findEntranceByCourierIdAndTime(
            @Param("courierId") Long courierId,
            @Param("since") LocalDateTime since
    );
}
