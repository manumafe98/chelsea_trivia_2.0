package com.manumafe.players.data.playersdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlayersdataApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(PlayersdataApplication.class, args);
	}
}
