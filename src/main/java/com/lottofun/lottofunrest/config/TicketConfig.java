package com.lottofun.lottofunrest.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "lottofun.ticket")
@Data
@Validated
public class TicketConfig {
    @Min(0)
    @Max(100)
    private double price = 10.0;
}
