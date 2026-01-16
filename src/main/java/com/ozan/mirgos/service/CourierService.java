package com.ozan.mirgos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ozan.mirgos.entity.Courier;
import com.ozan.mirgos.repository.CourierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourierService {
    private final CourierRepository courierRepository;

    @Transactional
    public Courier registerCourier(String name) {
        Courier courier = new Courier();
        courier.setName(name);
        return courierRepository.save(courier);
    }

    public Courier getCourierById(Long id) {
        return courierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courier not found with id: " + id));
    }
    
    public List<Courier> getCouriers() {
        return courierRepository.findAll();
    }
}
