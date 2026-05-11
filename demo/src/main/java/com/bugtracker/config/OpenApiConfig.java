package com.bugtracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bugTrackerOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Bug Tracker API")
                .description("REST API documentation for managing bugs and their lifecycle.")
                .version("v1")
                .contact(new Contact().name("Bug Tracker Team"))
                .license(new License().name("Internal Use")));
    }
}
