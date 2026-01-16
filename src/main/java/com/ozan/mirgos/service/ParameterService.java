package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.Parameter;
import com.ozan.mirgos.repository.ParameterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParameterService {
    private final ParameterRepository parameterRepository;

    // Parameter keys
    public static final String ENTRANCE_THRESHOLD_MINUTES = "entrance_event_threshold_minutes";
    public static final String PROXIMITY_RADIUS_METERS = "proximity_radius_meters";
    public static final String DISTANCE_STRATEGY = "distance_strategy";

    @Cacheable(value = "parameters", key = "#key")
    public String getParameterValue(String key, String defaultValue) {
        return parameterRepository.findByKey(key)
                .map(Parameter::getValue)
                .orElse(defaultValue);
    }

    @Cacheable(value = "parameters", key = "#key")
    public Double getParameterValueAsDouble(String key, Double defaultValue) {
        String value = getParameterValue(key, String.valueOf(defaultValue));
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Cacheable(value = "parameters", key = "#key")
    public Integer getParameterValueAsInteger(String key, Integer defaultValue) {
        String value = getParameterValue(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Transactional
    public Parameter setParameter(String key, String value, String description) {
        Parameter parameter = parameterRepository.findByKey(key)
                .orElse(new Parameter());
        parameter.setKey(key);
        parameter.setValue(value);
        parameter.setDescription(description);
        return parameterRepository.save(parameter);
    }

    public void evictCache(String key) {
        // Cache eviction will be handled by @CacheEvict if needed
        // For now, we rely on TTL or manual cache clearing
    }
}
