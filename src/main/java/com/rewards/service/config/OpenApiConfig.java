package com.rewards.service.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI api() {
		return new OpenAPI().info(new Info().title("Reward Service").version("1.0")
				.description("Interview Assignment – Senior Engineer"));
	}
}
