package com.ozan.mirgos.repository;

import com.ozan.mirgos.entity.Entrance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EntranceRepository extends JpaRepository<Entrance, Long> {
    
    @Query("SELECT e FROM Entrance e WHERE e.courierId = :courierId AND e.storeId = :storeId " +
           "AND e.eventTime >= :since")
    Optional<Entrance> findRecentEntrance(
            @Param("courierId") Long courierId,
            @Param("storeId") Long storeId,
            @Param("since") LocalDateTime since
    );
}
