package com.womentech.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
@RestController
public class ServerApplication {

	@GetMapping("/")
	public String hello() {
		return "쑥스러운 자매들";
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
