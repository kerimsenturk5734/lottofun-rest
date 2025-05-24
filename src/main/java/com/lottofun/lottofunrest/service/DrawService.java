package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.config.DrawConfig;
import com.lottofun.lottofunrest.model.Draw;
import com.lottofun.lottofunrest.model.DrawStatus;
import com.lottofun.lottofunrest.model.Ticket;
import com.lottofun.lottofunrest.model.TicketStatus;
import com.lottofun.lottofunrest.repository.DrawRepository;
import com.lottofun.lottofunrest.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DrawService {
    private final DrawRepository drawRepository;
    private final TicketService ticketService;
    private final DrawConfig drawConfig;

    /**
     * Represents the lifecycle stages of a lottery draw.
     * <p>
     * The draw transitions through the following states in order:
     * <pre>
     * OPEN → CLOSED → EXTRACTED → PAYMENT_PROCESSING → FINALIZED
     * </pre>
     *
     * <ul>
     *   <li><b>OPEN</b> - Users can buy tickets; winning numbers have not been drawn yet.</li>
     *   <li><b>CLOSED</b> - Ticket purchase is closed; waiting for winning numbers to be drawn.</li>
     *   <li><b>EXTRACTED</b> - Winning numbers have been drawn; payment processing not started.</li>
     *   <li><b>PAYMENT_PROCESSING</b> - Prizes are being calculated for the winning tickets.</li>
     *   <li><b>FINALIZED</b> - All draw processes are completed; the draw is now read-only.</li>
     * </ul>
     */
    public DrawService(DrawRepository drawRepository, TicketService ticketService, DrawConfig drawConfig) {
        this.drawRepository = drawRepository;
        this.ticketService = ticketService;
        this.drawConfig = drawConfig;
    }

    // returns draws that its winning numbers resulted. ordered by draw date
    public Page<Draw> getDrawHistory(Pageable pageable) {
        var historyDrawStatuses = List.of(DrawStatus.EXTRACTED, DrawStatus.PAYMENTS_PROCESSING, DrawStatus.FINALIZED);
        return drawRepository.findAllByStatusInOrderByDrawDate(historyDrawStatuses, pageable);
    }

    public boolean existsByStatus(DrawStatus status) {
        return drawRepository.existsByStatus(status);
    }

   public Draw saveDraw(Draw draw) {
        return drawRepository.save(draw);
   }

    public void processDraws() {
        Optional<Draw> currentDrawOpt = drawRepository.findFirstByStatusOrderByDrawDateAsc(DrawStatus.OPEN);

        if (currentDrawOpt.isEmpty()) return;

        Draw currentDraw = currentDrawOpt.get();

        // Eğer çekiliş zamanı geldiyse işlemleri başlat
        if (currentDraw.getDrawDate().isBefore(LocalDateTime.now())) {

            // 1. Satışı kapat
            currentDraw.setStatus(DrawStatus.CLOSED);
            saveDraw(currentDraw);

            // 2. Kazanan sayıları belirle
            Set<Integer> winningNumbers = generateWinningNumbers();
            currentDraw.setWinningNumbers(winningNumbers);
            currentDraw.setStatus(DrawStatus.EXTRACTED);
            saveDraw(currentDraw);

            // 3. Ödülleri hesapla
            currentDraw.setStatus(DrawStatus.PAYMENTS_PROCESSING);
            saveDraw(currentDraw);

            List<Ticket> tickets = ticketService.getTicketsByDrawId(currentDraw.getId());
            for (Ticket ticket : tickets) {
                int matched = (int) ticket.getNumbers().stream()
                        .filter(winningNumbers::contains)
                        .count();
                ticket.setMatchedCount(matched);
                ticket.setStatus(matched >= 2 ? TicketStatus.WON : TicketStatus.LOST);
                ticket.setPrize(determinePrize(matched));
                ticketService.updateTicket(ticket);
            }

            // 4. Çekilişi finalize et
            currentDraw.setStatus(DrawStatus.FINALIZED);
            saveDraw(currentDraw);

            // 5. Yeni çekilişi başlat
            Draw nextDraw = Draw.builder()
                    .drawDate(LocalDateTime.now().plus(drawConfig.getNextDrawInterval(), ChronoUnit.MILLIS))
                    .status(DrawStatus.OPEN)
                    .build();
            saveDraw(nextDraw);
        }
    }

    private Set<Integer> generateWinningNumbers() {
        // Use set for unique numbers
        Set<Integer> numbers = new HashSet<>();

        Random random = new Random();
        while (numbers.size() < 5) {
            numbers.add(random.nextInt(49) + 1);
        }
        return numbers;
    }

    private String determinePrize(int matchCount) {
        return switch (matchCount) {
            case 5 -> "Jackpot";
            case 4 -> "High";
            case 3 -> "Medium";
            case 2 -> "Low";
            default -> "No Prize";
        };
    }
}
