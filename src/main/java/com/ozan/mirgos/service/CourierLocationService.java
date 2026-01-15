package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.event.CourierLocationEnteredEvent;
import com.ozan.mirgos.repository.CourierLocationRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        
        // Publish event when courier location is entered
        eventPublisher.publishEvent(new CourierLocationEnteredEvent(this, saved));
        
        return saved;
    }
}
