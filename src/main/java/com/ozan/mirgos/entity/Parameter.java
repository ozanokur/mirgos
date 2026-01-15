package com.ozan.mirgos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    @Id
    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String value;

    @Column(length = 500)
    private String description;
}
