package com.ozan.mirgos.listener;

import com.ozan.mirgos.event.CourierLocationEnteredEvent;
import com.ozan.mirgos.service.EntranceEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourierLocationEventListener {
    private final EntranceEventService entranceEventService;

    @Async
    @EventListener
    public void handleCourierLocationEntered(CourierLocationEnteredEvent event) {
        log.info("Processing courier location event for courier ID: {}", event.getCourierLocation().getCourierId());
        entranceEventService.processCourierLocation(
                event.getCourierLocation().getCourierId(),
                event.getCourierLocation().getLocation()
        );
    }
}
