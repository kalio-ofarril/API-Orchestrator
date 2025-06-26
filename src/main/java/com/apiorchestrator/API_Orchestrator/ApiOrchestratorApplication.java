package com.apiorchestrator.API_Orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "API Orchestrator",
        version = "1.0",
        description = "API documentation for the scheduled API orchestrator"
    )
)
@SpringBootApplication
public class ApiOrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiOrchestratorApplication.class, args);
	}

}
