package com.ozan.mirgos.service;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ozan.mirgos.entity.CourierDistance;
import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.repository.CourierDistanceRepository;
import com.ozan.mirgos.service.distance.DistanceStrategySelector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistanceCalculationService {
    private final CourierDistanceRepository courierDistanceRepository;
    private final CourierLocationService courierLocationService;
    private final DistanceStrategySelector distanceStrategySelector;

    @Transactional
    public void calculateAndUpdateDistance(CourierLocation currentLocation) {
        
        // Get previous location for distance calculation
        CourierLocation previousLocation = courierLocationService.getPreviousLocation(currentLocation.getCourierId(), currentLocation.getId(), currentLocation.getTime());

        if (previousLocation == null) {
            // First location for this courier, initialize distance to 0
            CourierDistance courierDistance = courierDistanceRepository.findByCourierId(currentLocation.getCourierId())
                    .orElse(new CourierDistance());
            courierDistance.setCourierId(currentLocation.getCourierId());
            if (courierDistance.getTotalDistanceMeters() == null) {
                courierDistance.setTotalDistanceMeters(0.0);
            }
            courierDistanceRepository.save(courierDistance);
            return;
        }

        // Calculate distance between previous and current location using PostGIS
        double distance = calculateDistance(previousLocation.getLocation(), currentLocation.getLocation());

        // Update total distance for the courier
        CourierDistance courierDistance = courierDistanceRepository.findByCourierId(currentLocation.getCourierId())
                .orElse(new CourierDistance());
        
        courierDistance.setCourierId(currentLocation.getCourierId());
        double currentTotal = courierDistance.getTotalDistanceMeters() != null 
                ? courierDistance.getTotalDistanceMeters() 
                : 0.0;
        courierDistance.setTotalDistanceMeters(currentTotal + distance);
        
        courierDistanceRepository.save(courierDistance);
    }

    private double calculateDistance(Point point1, Point point2) {
        // Use PostGIS ST_Distance with geography for accurate distance in meters
        // This will be calculated via native query
        return distanceStrategySelector.current().calculate(point1, point2);
    }
}
