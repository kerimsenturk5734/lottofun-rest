package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.dto.TicketDto;
import com.lottofun.lottofunrest.mapper.TicketMapper;
import com.lottofun.lottofunrest.model.Ticket;
import com.lottofun.lottofunrest.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Page<TicketDto> getTicketsByUser_Username(String username, Pageable pageable) {
        var page = ticketRepository.findByUser_UsernameOrderByPurchaseTimeAsc(username, pageable);
        var mapper = TicketMapper.ticketAndTicketDto();
        return page.map(mapper::convert);
    }

    public List<Ticket> getTicketsByDrawId(Long drawId) {
        return ticketRepository.findByDrawId(drawId);
    }
}
