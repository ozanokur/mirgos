package com.ozan.mirgos.repository;

import com.ozan.mirgos.entity.Store;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    
    @Query(value = "SELECT * FROM stores WHERE ST_DWithin(location::geography, :point::geography, :distanceInMeters)", nativeQuery = true)
    List<Store> findStoresWithinDistance(@Param("point") Point point, @Param("distanceInMeters") double distanceInMeters);
}
