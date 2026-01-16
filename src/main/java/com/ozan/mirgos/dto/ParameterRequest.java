package com.ozan.mirgos.dto;

import lombok.Data;

@Data
public class ParameterRequest {
    private String key;
    private String value;
    private String description;
}
