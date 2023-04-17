package org.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TimeApiApp {

	public static void main(String[] args) {
		SpringApplication.run(TimeApiApp.class, args);
	}
	@Bean
	public RestTemplate timeApiTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
