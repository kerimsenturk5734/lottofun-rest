package com.lottofun.lottofunrest.job;

import com.lottofun.lottofunrest.config.DrawConfig;
import com.lottofun.lottofunrest.model.Draw;
import com.lottofun.lottofunrest.model.DrawStatus;
import com.lottofun.lottofunrest.service.DrawService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class DrawInitializer implements ApplicationRunner {

    private final DrawService drawService;
    private final DrawConfig drawConfig;

    public DrawInitializer(DrawService drawService, DrawConfig drawConfig) {
        this.drawService = drawService;
        this.drawConfig = drawConfig;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean hasOpenDraw = drawService.existsByStatus(DrawStatus.OPEN);

        if (!hasOpenDraw) {
            Draw initialDraw = Draw.builder()
                    .drawDate(LocalDateTime.now().plus(drawConfig.getNextDrawInterval(), ChronoUnit.MILLIS))
                    .status(DrawStatus.OPEN)
                    .build();

            drawService.saveDraw(initialDraw);
            System.out.println("Initial draw created: " + initialDraw.getDrawDate());
        }
    }
}
