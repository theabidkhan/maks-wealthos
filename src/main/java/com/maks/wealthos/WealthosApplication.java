package com.maks.wealthos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class WealthosApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				WealthosApplication.class,
				args
		);

		log.info("======================================");
		log.info("********** MAKs WealthOS Started Successfully **********");
		log.info("Swagger URL : http://localhost:8080/swagger-ui/index.html");
		log.info("Health URL  : http://localhost:8080/actuator/health");
		log.info("======================================");
	}
}