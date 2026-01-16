package com.ozan.mirgos.service.distance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component("postgisDistanceStrategy")
public class PostgisDistanceStrategy implements DistanceCalculationStrategy {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public double calculate(Point from, Point to) {
        return ((Number) entityManager.createNativeQuery("""
            SELECT ST_Distance(
                CAST(:p1 AS geography),
                CAST(:p2 AS geography)
            )
        """)
        .setParameter("p1", from)
        .setParameter("p2", to)
        .getSingleResult()).doubleValue();
    }
}
