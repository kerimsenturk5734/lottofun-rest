package com.lottofun.lottofunrest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lottofun.draw")
@Data
public class DrawConfig {
    private long nextDrawInterval;
}
