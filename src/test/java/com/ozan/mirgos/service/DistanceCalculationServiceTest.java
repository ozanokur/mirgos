package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.CourierDistance;
import com.ozan.mirgos.entity.CourierLocation;
import com.ozan.mirgos.repository.CourierDistanceRepository;
import com.ozan.mirgos.service.distance.DistanceCalculationStrategy;
import com.ozan.mirgos.service.distance.DistanceStrategySelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

class DistanceCalculationServiceTest {

    @Mock
    private CourierDistanceRepository courierDistanceRepository;

    @Mock
    private CourierLocationService courierLocationService;

    @Mock
    private DistanceStrategySelector distanceStrategySelector;

    @Mock
    private DistanceCalculationStrategy distanceCalculationStrategy;

    @InjectMocks
    private DistanceCalculationService distanceCalculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateAndUpdateDistance_FirstLocation() {
        CourierLocation currentLocation = new CourierLocation();
        currentLocation.setCourierId(1L);
        currentLocation.setId(1L);
        currentLocation.setTime(LocalDateTime.now());

        when(courierLocationService.getPreviousLocation(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(null);
        when(courierDistanceRepository.findByCourierId(anyLong()))
                .thenReturn(Optional.empty());

        distanceCalculationService.calculateAndUpdateDistance(currentLocation);

        verify(courierDistanceRepository, times(1)).save(any(CourierDistance.class));
    }

    @Test
    void testCalculateAndUpdateDistance_WithPreviousLocation() {
        CourierLocation currentLocation = new CourierLocation();
        currentLocation.setCourierId(1L);
        currentLocation.setId(1L);
        currentLocation.setTime(LocalDateTime.now());
        currentLocation.setLocation(mock(Point.class));

        CourierLocation previousLocation = new CourierLocation();
        previousLocation.setLocation(mock(Point.class));

        when(courierLocationService.getPreviousLocation(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(previousLocation);
        when(distanceStrategySelector.current())
                .thenReturn(distanceCalculationStrategy);
        when(distanceCalculationStrategy.calculate(any(Point.class), any(Point.class)))
                .thenReturn(100.0);
        when(courierDistanceRepository.findByCourierId(anyLong()))
                .thenReturn(Optional.of(new CourierDistance()));

        distanceCalculationService.calculateAndUpdateDistance(currentLocation);

        verify(courierDistanceRepository, times(1)).save(any(CourierDistance.class));
    }
}