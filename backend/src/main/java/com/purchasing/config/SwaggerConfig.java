package com.purchasing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion des Achats et Fournisseurs")
                        .version("1.0.0")
                        .description("Système complet de gestion des achats, commandes et évaluation fournisseurs"));
    }
}