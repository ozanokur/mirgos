package com.ozan.mirgos.controller;

import com.ozan.mirgos.dto.CourierRequest;
import com.ozan.mirgos.entity.Courier;
import com.ozan.mirgos.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierController {
    private final CourierService courierService;

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
}
