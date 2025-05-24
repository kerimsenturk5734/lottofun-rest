package com.lottofun.lottofunrest.job;

import com.lottofun.lottofunrest.config.DrawConfig;
import com.lottofun.lottofunrest.service.DrawService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DrawScheduler {
    private final DrawService drawService;
    private final DrawConfig drawConfig;

    public DrawScheduler(DrawService drawService, DrawConfig drawConfig) {
        this.drawService = drawService;
        this.drawConfig = drawConfig;
    }

    // Run after draw date has passed
    @Scheduled(fixedRateString = "#{@drawConfig.nextDrawInterval + 100}")
    public void scheduleDraw() {
        System.out.println("draw scheduled");
        drawService.processDraws();
    }
}
