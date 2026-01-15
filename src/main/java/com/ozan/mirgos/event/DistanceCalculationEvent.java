package com.ozan.mirgos.event;

import com.ozan.mirgos.entity.CourierLocation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DistanceCalculationEvent extends ApplicationEvent {
    private final CourierLocation courierLocation;
    private final CourierLocation previousLocation;

    public DistanceCalculationEvent(Object source, CourierLocation courierLocation, CourierLocation previousLocation) {
        super(source);
        this.courierLocation = courierLocation;
        this.previousLocation = previousLocation;
    }
}
