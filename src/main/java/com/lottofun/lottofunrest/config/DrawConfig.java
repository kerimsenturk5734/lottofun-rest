package com.lottofun.lottofunrest.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "lottofun.draw")
@Validated
@Data
public class DrawConfig {
    @Min(1000)
    private long nextDrawInterval = 100000;

    @Min(0)
    private long closeBeforeDrawMillis = 20000;


    @PostConstruct
    public void validateTimings() {
        if (closeBeforeDrawMillis >= nextDrawInterval) {
            throw new IllegalArgumentException("closeBeforeDrawMillis must be less than nextDrawInterval");
        }
    }
}
