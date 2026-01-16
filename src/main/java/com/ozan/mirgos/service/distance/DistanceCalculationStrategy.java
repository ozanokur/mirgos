package com.ozan.mirgos.service.distance;

import org.locationtech.jts.geom.Point;

public interface DistanceCalculationStrategy {
    double calculate(Point from, Point to);
}