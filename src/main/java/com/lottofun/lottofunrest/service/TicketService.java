package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.config.TicketConfig;
import com.lottofun.lottofunrest.dto.TicketDto;
import com.lottofun.lottofunrest.exception.DrawNotAvailableForPurchaseException;
import com.lottofun.lottofunrest.exception.InsufficientBalanceException;
import com.lottofun.lottofunrest.mapper.TicketMapper;
import com.lottofun.lottofunrest.model.DrawStatus;
import com.lottofun.lottofunrest.model.Ticket;
import com.lottofun.lottofunrest.model.TicketStatus;
import com.lottofun.lottofunrest.repository.TicketRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final DrawService drawService;
    private final UserService userService;
    private final TicketConfig ticketConfig;

    public TicketService(TicketRepository ticketRepository, @Lazy DrawService drawService, UserService userService, TicketConfig ticketConfig) {
        this.ticketRepository = ticketRepository;
        this.drawService = drawService;
        this.userService = userService;
        this.ticketConfig = ticketConfig;
    }

    public Page<TicketDto> getTicketsByUser_Username(String username, Pageable pageable) {
        var page = ticketRepository.findByUser_UsernameOrderByPurchaseTimeAsc(username, pageable);
        var mapper = TicketMapper.ticketAndTicketDto();
        return page.map(mapper::convert);
    }

    public List<Ticket> getTicketsByDrawId(Long drawId) {
        return ticketRepository.findByDrawId(drawId);
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }


    public TicketDto buyTicket(String username, long drawId, Set<Integer> numbers) {
        // get user
        var user = userService.getUserByUsername(username);

        var ticketPrice = ticketConfig.getPrice();

        // get related draw
        var draw = drawService.getDrawById(drawId);

        // check related draw is open
        if(!draw.getStatus().equals(DrawStatus.OPEN))
            throw new DrawNotAvailableForPurchaseException();

        // pay ticket price
        userService.withdraw(user.getUsername(), ticketPrice);

        // everything looks good create the ticket

        var ticket = Ticket.builder()
                .drawId(draw.getId())
                .user(user)
                .numbers(numbers)
                .purchaseTime(Instant.now())
                .status(TicketStatus.WAITING)
                .build();

         var newTicket = saveTicket(ticket);

         return TicketMapper.ticketAndTicketDto().convert(newTicket);
    }
}
