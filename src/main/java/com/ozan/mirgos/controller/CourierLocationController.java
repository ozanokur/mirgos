package com.ozan.mirgos.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ozan.mirgos.dto.CourierLocationRequest;
import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.service.CourierLocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Courier Locations",
    description = "Courier Location CRUD."
)
@RestController
@RequestMapping("/api/courier-locations")
@RequiredArgsConstructor
public class CourierLocationController {
    private final CourierLocationService courierLocationService;

    @Operation(
        summary = "Ingest courier locations",
        description = "Courier location stream ingestion. In a real project this would be done through an event system like rabbit or kafka, but here we used API for simplicity"
    )
    @PostMapping
    public ResponseEntity<CourierLocation> registerCourierLocation(@RequestBody CourierLocationRequest request) {
        if (request.getCourierId() == null || request.getLatitude() == null || request.getLongitude() == null) {
            return ResponseEntity.badRequest().build();
        }
        CourierLocation location = courierLocationService.registerCourierLocation(
                request.getCourierId(),
                request.getLatitude(),
                request.getLongitude(),
                request.getTime()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }
    
    @Profile("!prod")
    @Operation(
        summary = "List courier locations",
        description = "For testing purposes"
    )
    @GetMapping
    public ResponseEntity<List<CourierLocation>> getCourierLocations() {
        List<CourierLocation> locations = courierLocationService.getCourierLocations();
        return ResponseEntity.ok(locations);
    }
}
