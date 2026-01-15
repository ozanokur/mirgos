package com.ozan.mirgos.controller;

import com.ozan.mirgos.service.CourierDistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierDistanceController {
    private final CourierDistanceService courierDistanceService;

    @GetMapping("/{courierId}/total-distance")
    public ResponseEntity<TotalDistanceResponse> getTotalTravelDistance(@PathVariable Long courierId) {
        Double totalDistance = courierDistanceService.getTotalTravelDistance(courierId);
        return ResponseEntity.ok(new TotalDistanceResponse(courierId, totalDistance));
    }

    public record TotalDistanceResponse(Long courierId, Double totalDistanceMeters) {}
}
