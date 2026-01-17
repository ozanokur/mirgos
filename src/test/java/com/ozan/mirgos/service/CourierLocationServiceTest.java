package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.event.DistanceCalculationEvent;
import com.ozan.mirgos.event.EntranceCalculationEvent;
import com.ozan.mirgos.repository.CourierLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

class CourierLocationServiceTest {

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CourierLocationService courierLocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCourierLocation() {
        Long courierId = 1L;
        double latitude = 40.0;
        double longitude = -74.0;
        LocalDateTime time = LocalDateTime.now();

        CourierLocation courierLocation = new CourierLocation();
        courierLocation.setCourierId(courierId);
        courierLocation.setLocation(mock(Point.class));
        courierLocation.setTime(time);

        when(courierLocationRepository.save(any(CourierLocation.class))).thenReturn(courierLocation);

        courierLocationService.registerCourierLocation(courierId, latitude, longitude, time);

        verify(eventPublisher, times(1)).publishEvent(any(EntranceCalculationEvent.class));
        verify(eventPublisher, times(1)).publishEvent(any(DistanceCalculationEvent.class));
    }

    @Test
    void testGetCourierLocations() {
        Long courierId = 1L;
        LocalDateTime time = LocalDateTime.now();

        when(courierLocationRepository.findByCourierIdAndTime(courierId, time))
                .thenReturn(Collections.emptyList());

        courierLocationService.getCourierLocations(courierId, time);

        verify(courierLocationRepository, times(1)).findByCourierIdAndTime(courierId, time);
    }
}