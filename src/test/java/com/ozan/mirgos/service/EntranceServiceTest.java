package com.ozan.mirgos.service;

import com.ozan.mirgos.entity.Entrance;
import com.ozan.mirgos.entity.Store;
import com.ozan.mirgos.repository.EntranceRepository;
import com.ozan.mirgos.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EntranceServiceTest {

    @Mock
    private EntranceRepository entranceRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ParameterService parameterService;

    @InjectMocks
    private EntranceService entranceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessCourierLocation() {
        Long courierId = 1L;
        Point location = mock(Point.class);
        LocalDateTime courierLocationTime = LocalDateTime.now();

        when(parameterService.getParameterValueAsDouble(anyString(), anyDouble())).thenReturn(50.0);
        when(parameterService.getParameterValueAsInteger(anyString(), anyInt())).thenReturn(1);
        when(storeRepository.findStoresWithinDistance(any(Point.class), anyDouble()))
                .thenReturn(Collections.singletonList(new Store()));
        when(entranceRepository.findRecentEntrance(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        entranceService.processCourierLocation(courierId, location, courierLocationTime);

        verify(entranceRepository, times(1)).save(any(Entrance.class));
    }
    @Test
    void testProcessCourierLocation_WithinRadiusAndNoRecentEntrance() {
        Long courierId = 1L;
        Point location = mock(Point.class);
        LocalDateTime courierLocationTime = LocalDateTime.now();

        Store store = new Store();
        store.setId(1L);

        when(parameterService.getParameterValueAsDouble(anyString(), anyDouble())).thenReturn(100.0); // 100m radius
        when(parameterService.getParameterValueAsInteger(anyString(), anyInt())).thenReturn(1); // 1-minute threshold
        when(storeRepository.findStoresWithinDistance(any(Point.class), eq(100.0)))
                .thenReturn(Collections.singletonList(store));
        when(entranceRepository.findRecentEntrance(eq(courierId), eq(store.getId()), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        entranceService.processCourierLocation(courierId, location, courierLocationTime);

        verify(entranceRepository, times(1)).save(any(Entrance.class));
    }

    @Test
    void testProcessCourierLocation_ReEntrySuppression() {
        Long courierId = 1L;
        Point location = mock(Point.class);
        LocalDateTime courierLocationTime = LocalDateTime.now();

        Store store = new Store();
        store.setId(1L);

        when(parameterService.getParameterValueAsDouble(anyString(), anyDouble())).thenReturn(100.0); // 100m radius
        when(parameterService.getParameterValueAsInteger(anyString(), anyInt())).thenReturn(1); // 1-minute threshold
        when(storeRepository.findStoresWithinDistance(any(Point.class), eq(100.0)))
                .thenReturn(Collections.singletonList(store));
        when(entranceRepository.findRecentEntrance(eq(courierId), eq(store.getId()), any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Entrance())); // Recent entrance exists

        entranceService.processCourierLocation(courierId, location, courierLocationTime);

        verify(entranceRepository, never()).save(any(Entrance.class)); // No new entrance should be saved
    }

    @Test
    void testGetEntrances() {
        Long courierId = 1L;
        LocalDateTime from = LocalDateTime.now();

        entranceService.getEntrances(courierId, from);

        verify(entranceRepository, times(1)).findEntranceByCourierIdAndTime(courierId, from);
    }
}