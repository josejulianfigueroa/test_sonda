package com.sonda.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Búsqueda de Operacion API",
                version = "1.0.0",
                description = "Servicio que implementa la búsqueda de un numero de registro y devuelve su operación asociada.",
                contact = @Contact(
                        name = "Sonda Test",
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

