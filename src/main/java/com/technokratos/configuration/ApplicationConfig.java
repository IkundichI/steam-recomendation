package com.technokratos.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class ApplicationConfig {

    @Bean
    public OpenAPI publicApi() {
        return new OpenAPI().info(apiInfo());
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    private Info apiInfo() {
        return new Info()
                .title("Steam Recommender API")
                .description("-- API Documentation for Steam Recommender --")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Artem Valeev")
                        .url("-")
                        .email("artemi.arte.valeev@outlook.com"));
    }
}
