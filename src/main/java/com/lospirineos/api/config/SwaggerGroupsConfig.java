package com.lospirineos.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupsConfig {

    @Bean
    public GroupedOpenApi adminApi() {

        return GroupedOpenApi.builder()
                .group("Administración")
                .pathsToMatch(
                        "/api/v1/users/**",
                        "/api/v1/distributors/**",
                        "/api/v1/shifts/**",
                        "/api/v1/cash-registers/**",
                        "/api/v1/closings/**",
                        "/api/v1/transfers/**",
                        "/api/v1/movements/**",
                        "/api/v1/collection/**",
                        "/api/v1/dashboard/admin/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi waiterApi() {

        return GroupedOpenApi.builder()
                .group("Camarero")
                .pathsToMatch(
                        "/api/v1/auth/**",
                        "/api/v1/distributors/active",
                        "/api/v1/shifts/active",
                        "/api/v1/cash-registers/active",
                        "/api/v1/closings/**",
                        "/api/v1/transfers/**",
                        "/api/v1/movements/**",
                        "/api/v1/collection/mine/**",
                        "/api/v1/dashboard/waiter/**"
                )
                .build();
    }
}