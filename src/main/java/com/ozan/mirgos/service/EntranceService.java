package com.ozan.mirgos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ozan.mirgos.entity.Entrance;
import com.ozan.mirgos.entity.Store;
import com.ozan.mirgos.repository.EntranceRepository;
import com.ozan.mirgos.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntranceService {
    private final EntranceRepository entranceRepository;
    private final StoreRepository storeRepository;
    private final ParameterService parameterService;
    
    // Default values (used if parameter not found in DB)
    private static final double DEFAULT_PROXIMITY_RADIUS_METERS = 50.0;
    private static final int DEFAULT_ENTRANCE_THRESHOLD_MINUTES = 1;

    @Transactional
    public void processCourierLocation(Long courierId, Point location) {
        // Get proximity radius from database parameter (cached)
        double proximityRadius = parameterService.getParameterValueAsDouble(
                ParameterService.PROXIMITY_RADIUS_METERS, 
                DEFAULT_PROXIMITY_RADIUS_METERS
        );
        
        // Find stores within proximity
        List<Store> nearbyStores = storeRepository.findStoresWithinDistance(location, proximityRadius);
        
        LocalDateTime now = LocalDateTime.now();
        
        // Get entrance event threshold from database parameter (cached)
        int thresholdMinutes = parameterService.getParameterValueAsInteger(
                ParameterService.ENTRANCE_THRESHOLD_MINUTES,
                DEFAULT_ENTRANCE_THRESHOLD_MINUTES
        );
        
        // Prevent duplicate events within threshold minutes
        LocalDateTime thresholdTime = now.minusMinutes(thresholdMinutes);
        
        // Create entrance events for each nearby store (avoid duplicates)
        for (Store store : nearbyStores) {
            // Check if there's already a recent entrance for this courier-store combination
            // TODO: Bulk query entrances for stores, instead of individually
            boolean alreadyExists = entranceRepository.findRecentEntrance(
                    courierId, store.getId(), thresholdTime
            ).isPresent();
            
            if (!alreadyExists) {
                Entrance entrance = new Entrance();
                entrance.setCourierId(courierId);
                entrance.setStoreId(store.getId());
                entrance.setEventTime(now);
                entranceRepository.save(entrance);
            }
            log.info("[ENTRANCE_EVENT] Store id: {}, Courier id: {}", store.getId(), courierId);
        }
    }
}
