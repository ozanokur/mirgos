package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.EntranceEvent;
import com.ozan.mirgos.entity.Store;
import com.ozan.mirgos.repository.EntranceEventRepository;
import com.ozan.mirgos.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntranceEventService {
    private final EntranceEventRepository entranceEventRepository;
    private final StoreRepository storeRepository;
    private final ParameterService parameterService;
    
    // Default values (used if parameter not found in DB)
    private static final double DEFAULT_PROXIMITY_RADIUS_METERS = 50.0;
    private static final int DEFAULT_ENTRANCE_EVENT_THRESHOLD_MINUTES = 1;

    @Transactional
    public void processCourierLocation(Long courierId, org.locationtech.jts.geom.Point location) {
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
                ParameterService.ENTRANCE_EVENT_THRESHOLD_MINUTES,
                DEFAULT_ENTRANCE_EVENT_THRESHOLD_MINUTES
        );
        
        // Prevent duplicate events within threshold minutes
        LocalDateTime thresholdTime = now.minusMinutes(thresholdMinutes);
        
        // Create entrance events for each nearby store (avoid duplicates)
        for (Store store : nearbyStores) {
            // Check if there's already a recent entrance event for this courier-store combination
            boolean alreadyExists = entranceEventRepository.findRecentEntranceEvent(
                    courierId, store.getId(), thresholdTime
            ).isPresent();
            
            if (!alreadyExists) {
                EntranceEvent entranceEvent = new EntranceEvent();
                entranceEvent.setCourierId(courierId);
                entranceEvent.setStoreId(store.getId());
                entranceEvent.setEventTime(now);
                entranceEventRepository.save(entranceEvent);
            }
        }
    }
}
