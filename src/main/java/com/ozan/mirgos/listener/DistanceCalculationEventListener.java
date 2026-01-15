package com.ozan.mirgos.listener;

import com.ozan.mirgos.event.DistanceCalculationEvent;
import com.ozan.mirgos.service.DistanceCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DistanceCalculationEventListener {
    private final DistanceCalculationService distanceCalculationService;

    @Async
    @EventListener
    public void handleDistanceCalculation(DistanceCalculationEvent event) {
        log.debug("Processing distance calculation event for courier ID: {}", 
                event.getCourierLocation().getCourierId());
        distanceCalculationService.calculateAndUpdateDistance(
                event.getCourierLocation(),
                event.getPreviousLocation()
        );
    }
}
