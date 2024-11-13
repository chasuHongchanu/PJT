package com.example.trend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TREND")
                        .version("1.0.0")
                        .description("요즘 여행의 TREND(Trip Lend)")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("support@example.com")));
    }
}
