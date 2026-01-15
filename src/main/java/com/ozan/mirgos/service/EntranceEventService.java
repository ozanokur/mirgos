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
    
    // Proximity threshold in meters (default 50 meters)
    private static final double PROXIMITY_THRESHOLD_METERS = 50.0;

    @Transactional
    public void processCourierLocation(Long courierId, org.locationtech.jts.geom.Point location) {
        // Find stores within proximity
        List<Store> nearbyStores = storeRepository.findStoresWithinDistance(location, PROXIMITY_THRESHOLD_METERS);
        
        LocalDateTime now = LocalDateTime.now();
        // Prevent duplicate events within 5 minutes
        LocalDateTime fiveMinutesAgo = now.minusMinutes(5);
        
        // Create entrance events for each nearby store (avoid duplicates)
        for (Store store : nearbyStores) {
            // Check if there's already a recent entrance event for this courier-store combination
            boolean alreadyExists = entranceEventRepository.findRecentEntranceEvent(
                    courierId, store.getId(), fiveMinutesAgo
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
