package com.lospirineos.api.config;

import com.lospirineos.api.security.jwt.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
        JwtProperties.class
)
public class PropertiesConfig {
}