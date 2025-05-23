package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.model.Draw;
import com.lottofun.lottofunrest.model.DrawStatus;
import com.lottofun.lottofunrest.repository.DrawRepository;
import com.lottofun.lottofunrest.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrawService {
    private final DrawRepository drawRepository;

    /**
     * Draw lifecycle
     * OPEN -> CLOSED -> EXTRACTED -> PAYMENT_PROCESSING -> FINALIZED
     * <p>
     * OPEN => Users can buy ticket, winning numbers not resulted yet
     * CLOSE => Users can't buy ticket, waiting for winning numbers resulted
     * EXTRACTED => Winning numbers resulted, payment have not processed yet
     * PAYMENT => Prize calculating for tickets of this draw
     * FINALIZED => All draw processes completed. It is read only
     */
    public DrawService(DrawRepository drawRepository) {
        this.drawRepository = drawRepository;
    }

    // returns draws that its winning numbers resulted. ordered by draw date
    public Page<Draw> getDrawHistory(Pageable pageable) {
        var historyDrawStatuses = List.of(DrawStatus.EXTRACTED, DrawStatus.PAYMENTS_PROCESSING, DrawStatus.FINALIZED);
        return drawRepository.findAllByStatusInOrderByDrawDate(historyDrawStatuses, pageable);
    }
}
