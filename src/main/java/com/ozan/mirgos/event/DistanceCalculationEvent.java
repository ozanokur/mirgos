package com.ozan.mirgos.event;

import com.ozan.mirgos.entity.CourierLocation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DistanceCalculationEvent extends ApplicationEvent {
    private final CourierLocation courierLocation;

    public DistanceCalculationEvent(Object source, CourierLocation courierLocation) {
        super(source);
        this.courierLocation = courierLocation;
    }
}
