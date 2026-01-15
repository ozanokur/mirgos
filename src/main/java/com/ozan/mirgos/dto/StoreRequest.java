package com.ozan.mirgos.dto;

import lombok.Data;

@Data
public class StoreRequest {
    private String name;
    private Double latitude;
    private Double longitude;
}
