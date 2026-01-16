package com.ozan.mirgos.controller;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ozan.mirgos.dto.ParameterRequest;
import com.ozan.mirgos.service.ParameterService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Profile("!prod")
@Tag(
    name = "Utility",
    description = "Utility related endpoints fo testing purposes"
)
@RestController
@RequestMapping("/api/utility")
@RequiredArgsConstructor
public class UtilityController {
    private final CacheManager cacheManager;
    private final ParameterService parameterService;
    private final EntityManager entityManager;

    @PostMapping("/delete-caches")
    public ResponseEntity<String> deleteCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        return ResponseEntity.ok("Caches deleted");
    }

    // set parameter
    @PostMapping("/set-parameter")
    public ResponseEntity<String> setParameter(@RequestBody ParameterRequest request) {
        parameterService.setParameter(request.getKey(), request.getValue(), request.getDescription());
        return ResponseEntity.ok("Parameter set");
    }

    // truncate all tables and recreate them
    @PostMapping("/truncate-tables")
    @Transactional
    public ResponseEntity<String> truncateTables() {
        entityManager.createNativeQuery("""
                TRUNCATE TABLE
                    stores,
                    couriers,
                    courier_distances,
                    courier_locations,
                    entrances,
                    parameters
                CASCADE;
                                    """).executeUpdate();
        return ResponseEntity.ok("Tables truncated");
    }
}