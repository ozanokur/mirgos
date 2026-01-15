package com.ozan.mirgos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courier_distances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierDistance {
    @Id
    @Column(nullable = false)
    private Long courierId;

    @Column(nullable = false)
    private Double totalDistanceMeters;
}
