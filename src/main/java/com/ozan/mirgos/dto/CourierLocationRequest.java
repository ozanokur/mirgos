package com.ozan.mirgos.dto;

import lombok.Data;

@Data
public class CourierLocationRequest {
    private Long courierId;
    private Double latitude;
    private Double longitude;
}
