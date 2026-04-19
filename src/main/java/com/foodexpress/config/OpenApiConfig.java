package com.foodexpress.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI foodExpressOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FoodExpress API")
                        .version("1.0")
                        .description("API de commande et livraison de nourriture")
                        .contact(new Contact().name("FoodExpress Team")));
    }
}