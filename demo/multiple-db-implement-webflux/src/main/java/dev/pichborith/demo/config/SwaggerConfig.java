package dev.pichborith.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                   .info(new Info()
                             .title("Spring WebFlux with MongoDB API")
                             .version("1.0")
                             .description("Reactive REST API for managing users with MongoDB."));
    }
}
