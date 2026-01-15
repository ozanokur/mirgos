package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.event.CourierLocationEnteredEvent;
import com.ozan.mirgos.event.DistanceCalculationEvent;
import com.ozan.mirgos.repository.CourierLocationRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierLocationService {
    private final CourierLocationRepository courierLocationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional
    public CourierLocation registerCourierLocation(Long courierId, double latitude, double longitude) {
        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        CourierLocation courierLocation = new CourierLocation();
        courierLocation.setCourierId(courierId);
        courierLocation.setLocation(location);
        courierLocation.setTime(LocalDateTime.now());
        
        CourierLocation saved = courierLocationRepository.save(courierLocation);
        
        // Get previous location for distance calculation
        CourierLocation previousLocation = getPreviousLocation(courierId, saved.getId());
        
        // Publish event when courier location is entered
        eventPublisher.publishEvent(new CourierLocationEnteredEvent(this, saved));
        
        // Publish distance calculation event (separate event for performance)
        eventPublisher.publishEvent(new DistanceCalculationEvent(this, saved, previousLocation));
        
        return saved;
    }

    private CourierLocation getPreviousLocation(Long courierId, Long currentLocationId) {
        List<CourierLocation> previousLocations = courierLocationRepository.findPreviousLocationByCourierId(
                courierId, currentLocationId, PageRequest.of(0, 1)
        );
        return previousLocations.isEmpty() ? null : previousLocations.get(0);
    }
}
