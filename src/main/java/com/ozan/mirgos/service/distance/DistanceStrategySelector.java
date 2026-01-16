package com.ozan.mirgos.service.distance;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ozan.mirgos.service.ParameterService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DistanceStrategySelector {
    private final ParameterService parameterService;
    private final Map<String, DistanceCalculationStrategy> strategies;

    public DistanceCalculationStrategy current() {
        // Get proximity radius from database parameter (cached)
        String currentStrategy = parameterService.getParameterValue(
                ParameterService.DISTANCE_STRATEGY, 
                "haversineDistanceStrategy"
        );
        return strategies.get(currentStrategy);
    }
}