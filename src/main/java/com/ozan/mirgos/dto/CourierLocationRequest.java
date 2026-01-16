package com.ozan.mirgos.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CourierLocationRequest {
    private Long courierId;
    private Double latitude;
    private Double longitude;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime time;
}
