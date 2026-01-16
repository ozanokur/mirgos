package com.ozan.mirgos.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ozan.mirgos.event.EntranceCalculationEvent;
import com.ozan.mirgos.service.EntranceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntranceCalculationEventListener {
    private final EntranceService entranceService;

    @Async
    @EventListener
    public void handleCourierLocationEntered(EntranceCalculationEvent event) {
        log.info("Processing courier location event for courier ID: {}", event.getCourierLocation().getCourierId());
        entranceService.processCourierLocation(
                event.getCourierLocation().getCourierId(),
                event.getCourierLocation().getLocation()
        );
    }
}
