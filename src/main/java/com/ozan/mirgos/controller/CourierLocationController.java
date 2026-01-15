package com.ozan.mirgos.controller;

import com.ozan.mirgos.dto.CourierLocationRequest;
import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.service.CourierLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courier-locations")
@RequiredArgsConstructor
public class CourierLocationController {
    private final CourierLocationService courierLocationService;

    @PostMapping
    public ResponseEntity<CourierLocation> registerCourierLocation(@RequestBody CourierLocationRequest request) {
        if (request.getCourierId() == null || request.getLatitude() == null || request.getLongitude() == null) {
            return ResponseEntity.badRequest().build();
        }
        CourierLocation location = courierLocationService.registerCourierLocation(
                request.getCourierId(),
                request.getLatitude(),
                request.getLongitude()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }
}
