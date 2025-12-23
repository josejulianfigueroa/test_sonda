package com.hackerrank.sample.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Item Comparison API",
                version = "1.0.0",
                description = "Simplified backend API that provides item details for product comparison.",
                contact = @Contact(
                        name = "Hackerrank Sample",
                        email = "support@example.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://springdoc.org"
                )
        )
)
@Configuration
public class OpenApiConfig {
    // Empty: annotations above are enough for basic OpenAPI metadata.
}

