package com.lottofun.lottofunrest.job;

import com.lottofun.lottofunrest.service.DrawService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DrawScheduler {
    private final DrawService drawService;

    public DrawScheduler(DrawService drawService) {
        this.drawService = drawService;
    }

    @Scheduled(fixedDelayString = "#{drawConfig.closeBeforeDrawMillis / 2}")
    public void scheduleProcessDraws(){
        System.out.println("Schedule: Close Draws, Status: Started, Time: " + Instant.now());
        drawService.closeDraws();
        System.out.println("Schedule: Close Draws, Status: Finished, Time: " + Instant.now());

        System.out.println("Schedule: Extract Draws, Status: Started, Time: " + Instant.now());
        drawService.extractDraws();
        System.out.println("Schedule: Extract Draws, Status: Finished, Time: " + Instant.now());

        System.out.println("Schedule: Payment Draws, Status: Started, Time: " + Instant.now());
        drawService.processPayments();
        System.out.println("Schedule: Payment Draws, Status: Finished, Time: " + Instant.now());

        System.out.println("Schedule: Finalize Draws, Status: Started, Time: " + Instant.now());
        drawService.finalizeDrawsAndCreateNew();
        System.out.println("Schedule: Finalize Draws, Status: Finished, Time: " + Instant.now());


    }
}
