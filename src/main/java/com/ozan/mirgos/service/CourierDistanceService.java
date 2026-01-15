package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.CourierDistance;
import com.ozan.mirgos.repository.CourierDistanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourierDistanceService {
    private final CourierDistanceRepository courierDistanceRepository;

    public Double getTotalTravelDistance(Long courierId) {
        return courierDistanceRepository.findByCourierId(courierId)
                .map(CourierDistance::getTotalDistanceMeters)
                .orElse(0.0);
    }
}
