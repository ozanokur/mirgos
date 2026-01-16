package com.ozan.mirgos.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ozan.mirgos.dto.CourierRequest;
import com.ozan.mirgos.entity.Courier;
import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.service.CourierDistanceService;
import com.ozan.mirgos.service.CourierLocationService;
import com.ozan.mirgos.service.CourierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Couriers",
    description = "Courier CRUD. Also able to query total traveled distance and store entrance events"
)
@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierController {
    private final CourierService courierService;
    private final CourierLocationService courierLocationService;
    private final CourierDistanceService courierDistanceService;

    @PostMapping
    public ResponseEntity<Courier> registerCourier(@RequestBody CourierRequest request) {
        if (request.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        Courier courier = courierService.registerCourier(request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(courier);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Courier> getCourier(@PathVariable Long id) {
        try {
            Courier courier = courierService.getCourierById(id);
            return ResponseEntity.ok(courier);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/locations")
    public ResponseEntity<List<CourierLocation>> getCourierLocations(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from) {
        List<CourierLocation> locations = courierLocationService.getCourierLocations(id, from);
        return ResponseEntity.ok(locations);
    }

    @Operation(
        summary = "Courier entrance events",
        description = "Instances when a courier enters the store radius"
    )
    @GetMapping("/{id}/entrances")
    public ResponseEntity<List<CourierLocation>> getCourierEntrances(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from) {
        List<CourierLocation> locations = courierLocationService.getCourierLocations(id, from);
        return ResponseEntity.ok(locations);
    }

    @GetMapping
    public ResponseEntity<List<Courier>> getCouriers() {
        List<Courier> couriers = courierService.getCouriers();
        return ResponseEntity.ok(couriers);
    }

    @GetMapping("/{id}/total-distance")
    public ResponseEntity<TotalDistanceResponse> getTotalTravelDistance(@PathVariable Long id) {
        Double totalDistance = courierDistanceService.getTotalTravelDistance(id);
        return ResponseEntity.ok(new TotalDistanceResponse(id, totalDistance));
    }

    public record TotalDistanceResponse(Long courierId, Double totalDistanceMeters) {}
}
