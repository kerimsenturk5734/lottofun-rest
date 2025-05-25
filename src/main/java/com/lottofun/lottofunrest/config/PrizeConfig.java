package com.lottofun.lottofunrest.config;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "lottofun.prize")
@Data
@Validated
public class PrizeConfig {
    /**
     * These rates will multiply with ticket price on ticket won
     */
    @Min(0)
    private double jackpotFactor = 3.0;
    @Min(0)
    private double highFactor = 2.5;
    @Min(0)
    private double mediumFactor = 2.0;
    @Min(0)
    private double lowFactor = 1.5;
}
