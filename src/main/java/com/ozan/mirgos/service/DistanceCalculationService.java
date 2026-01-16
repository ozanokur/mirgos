package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.CourierDistance;
import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.repository.CourierDistanceRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DistanceCalculationService {
    private final CourierDistanceRepository courierDistanceRepository;
    private final CourierLocationService courierLocationService;

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
        return calculateDistanceNative(point1, point2);
    }

    private double calculateDistanceNative(Point point1, Point point2) {
        // We'll use a native query to calculate distance
        // For now, using a simple approach - in production, you might want to use a repository method
        // Using Haversine formula approximation for WGS84 coordinates
        double lat1 = point1.getY();
        double lon1 = point1.getX();
        double lat2 = point2.getY();
        double lon2 = point2.getX();

        final int R = 6371000; // Earth radius in meters

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
