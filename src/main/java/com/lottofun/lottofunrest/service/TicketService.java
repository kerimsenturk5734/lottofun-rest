package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.model.Ticket;
import com.lottofun.lottofunrest.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Page<Ticket> getTicketsByUser_Username(String username, Pageable pageable) {
        return ticketRepository.findByUser_UsernameOrderByPurchaseTimeAsc(username, pageable);
    }
}
