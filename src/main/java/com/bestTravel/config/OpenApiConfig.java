package com.bestTravel.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Best Travel Api", version = "1.0.0SNAPSHOT",
        description = "Documentacion para enpoints de Best Travel")
)
public class OpenApiConfig {
}
