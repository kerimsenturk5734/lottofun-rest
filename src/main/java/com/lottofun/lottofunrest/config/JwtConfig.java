package com.lottofun.lottofunrest.config;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@Validated
public class JwtConfig {
    @Size(min = 32, message = "JWT secret key must be at least 32 characters long")
    private String secret;
    private long expiration;
}