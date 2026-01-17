package com.ozan.mirgos.service.distance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HaversineDistanceStrategyTest {

    private HaversineDistanceStrategy haversineDistanceStrategy;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        haversineDistanceStrategy = new HaversineDistanceStrategy();
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void testCalculateDistance() {
        // Coordinates for two points
        Point point1 = geometryFactory.createPoint(new Coordinate(-74.005974, 40.712776)); // New York
        Point point2 = geometryFactory.createPoint(new Coordinate(-118.243683, 34.052235)); // Los Angeles

        // Expected distance (approximate, in meters)
        double expectedDistance = 3944000; // ~3944 km

        double calculatedDistance = haversineDistanceStrategy.calculate(point1, point2);

        // Allow a small margin of error
        assertEquals(expectedDistance, calculatedDistance, 10000); // 10 km margin
    }

    @Test
    void testCalculateDistance_SamePoint() {
        Point point = geometryFactory.createPoint(new Coordinate(-74.005974, 40.712776)); // New York

        double calculatedDistance = haversineDistanceStrategy.calculate(point, point);

        assertEquals(0.0, calculatedDistance, 0.001); // Distance should be 0
    }
}