package com.ozan.mirgos.service.distance;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component("haversineDistanceStrategy")
public class HaversineDistanceStrategy implements DistanceCalculationStrategy {

    @Override
    public double calculate(Point point1, Point point2) {
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
