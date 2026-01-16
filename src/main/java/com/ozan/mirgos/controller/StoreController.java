package com.ozan.mirgos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ozan.mirgos.dto.StoreRequest;
import com.ozan.mirgos.entity.Store;
import com.ozan.mirgos.service.StoreService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Stores",
    description = "Store CRUD"
)
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<Store> registerStore(@RequestBody StoreRequest request) {
        if (request.getName() == null || request.getLatitude() == null || request.getLongitude() == null) {
            return ResponseEntity.badRequest().build();
        }
        Store store = storeService.registerStore(request.getName(), request.getLatitude(), request.getLongitude());
        return ResponseEntity.status(HttpStatus.CREATED).body(store);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStore(@PathVariable Long id) {
        try {
            Store store = storeService.getStoreById(id);
            return ResponseEntity.ok(store);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Store>> getStores() {
        List<Store> stores = storeService.getStores();
        return ResponseEntity.ok(stores);
    }
}
