package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.config.DrawConfig;
import com.lottofun.lottofunrest.exception.NotFoundException;
import com.lottofun.lottofunrest.model.Draw;
import com.lottofun.lottofunrest.model.DrawStatus;
import com.lottofun.lottofunrest.model.Ticket;
import com.lottofun.lottofunrest.model.TicketStatus;
import com.lottofun.lottofunrest.repository.DrawRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
     *   <li><b>PAYMENT_DONE</b> - Ticket payments have been finished.</li>
     *   <li><b>FINALIZED</b> - All draw processes are completed; the draw is now read-only.</li>
     * </ul>
     */
    public DrawService(DrawRepository drawRepository, TicketService ticketService, DrawConfig drawConfig) {
        this.drawRepository = drawRepository;
        this.ticketService = ticketService;
        this.drawConfig = drawConfig;
    }

    public Draw getDrawById(long drawId) {
        return drawRepository.findById(drawId)
                .orElseThrow(() -> new NotFoundException("Draw", "Id", drawId));
    }

    // returns draws that its winning numbers resulted. ordered by draw date
    public Page<Draw> getDrawHistory(Pageable pageable) {
        var historyDrawStatuses = List.of(DrawStatus.EXTRACTED, DrawStatus.PAYMENTS_PROCESSING, DrawStatus.PAYMENT_DONE, DrawStatus.FINALIZED);
        return drawRepository.findAllByStatusInOrderByDrawDateDesc(historyDrawStatuses, pageable);
    }

    // returns active draws
    public List<Draw> getActiveDraws() {
        return drawRepository.findByStatus(DrawStatus.OPEN);
    }

    public boolean existsByStatus(DrawStatus status) {
        return drawRepository.existsByStatus(status);
    }

   public Draw saveDraw(Draw draw) {
        return drawRepository.save(draw);
   }

    public void closeDraws() {
        Instant now = Instant.now();

        // get open draws to try close
        List<Draw> drawsToClose = drawRepository.findByStatus(DrawStatus.OPEN);
        for (Draw draw : drawsToClose) {

            // get when this draw must close. it must close before draw date has come
            Instant closeTime = draw.getDrawDate().minusMillis(drawConfig.getCloseBeforeDrawMillis());

            // close if close time has come
            if (now.isAfter(closeTime)) {
                // For demonstration
                sleepDemonstration(10000);

                draw.setStatus(DrawStatus.CLOSED);
                draw.setStatusUpdatedAt(now);
                drawRepository.save(draw);
            }
        }
    }

    public void extractDraws() {
        Instant now = Instant.now();

        // get closed draws to extract winning numbers
        List<Draw> closedDraws = drawRepository.findByStatus(DrawStatus.CLOSED);

        for (Draw draw : closedDraws) {
            // extract if draw date has come
            if (now.isAfter(draw.getDrawDate())) {
                // For demonstration
                sleepDemonstration(10000);

                // generate winning numbers randomly
                Set<Integer> numbers = generateWinningNumbers();

                // update draw
                draw.setWinningNumbers(numbers);
                draw.setStatus(DrawStatus.EXTRACTED);
                draw.setStatusUpdatedAt(now);
                drawRepository.save(draw);
            }
        }
    }

    public void processPayments() {
        Instant now = Instant.now();

        // get extracted draws to start payment process
        List<Draw> extractedDraws = drawRepository.findByStatus(DrawStatus.EXTRACTED);

        for (Draw draw : extractedDraws) {


            // update draw status as payment processing
            draw.setStatus(DrawStatus.PAYMENTS_PROCESSING);
            draw.setStatusUpdatedAt(now);
            var processingDraw = drawRepository.save(draw);

            // For demonstration
            sleepDemonstration(10000);

            // get tickets related with this draw
            List<Ticket> tickets = ticketService.getTicketsByDrawId(draw.getId());

            for (Ticket ticket : tickets) {
                // calculate matched number count
                int matched = (int) ticket.getNumbers().stream()
                        .filter(draw.getWinningNumbers()::contains)
                        .count();

                ticket.setMatchedCount(matched);
                ticket.setStatus(matched >= 2 ? TicketStatus.WON : TicketStatus.LOST);
                ticket.setPrize(determinePrize(matched));
                ticketService.saveTicket(ticket);
            }

            // update draw status as payment done
            processingDraw.setStatus(DrawStatus.PAYMENT_DONE);
            processingDraw.setStatusUpdatedAt(now);
            drawRepository.save(processingDraw);
        }
    }

    public void finalizeDrawsAndCreateNew() {
        Instant now = Instant.now();

        // get payment done draws to finalize
        List<Draw> paymentProcessedDraws = drawRepository.findByStatus(DrawStatus.PAYMENT_DONE);

        for (Draw draw : paymentProcessedDraws) {
            // For demonstration
            sleepDemonstration(10000);

            // payment done, finalize the draw
            draw.setStatus(DrawStatus.FINALIZED);
            draw.setStatusUpdatedAt(now);
            drawRepository.save(draw);

            createNewDraw();
        }
    }

    private Draw createNewDraw() {
        Draw newDraw = Draw.builder()
                .drawDate(Instant.now().plus(drawConfig.getNextDrawInterval(), ChronoUnit.MILLIS))
                .status(DrawStatus.OPEN)
                .statusUpdatedAt(Instant.now())
                .build();

        var draw =  saveDraw(newDraw);

        System.out.println(draw.toString());

        return draw;
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

    private void sleepDemonstration(long delay){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
