package com.ozan.mirgos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class MirgosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MirgosApplication.class, args);
	}

}
