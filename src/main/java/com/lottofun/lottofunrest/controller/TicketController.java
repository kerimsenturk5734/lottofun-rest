package com.lottofun.lottofunrest.controller;

import com.lottofun.lottofunrest.dto.request.PageableRequest;
import com.lottofun.lottofunrest.dto.wrapper.PagedApiResult;
import com.lottofun.lottofunrest.model.Ticket;
import com.lottofun.lottofunrest.service.TicketService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/me")
    public ResponseEntity<PagedApiResult<Ticket>> getMyTickets(
            @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute PageableRequest pageableRequest) {
        var pageable = Pageable.ofSize(pageableRequest.getSize()).withPage(pageableRequest.getPage());

        var myTickets = ticketService.getTicketsByUser_Username(userDetails.getUsername(), pageable);

        var status = HttpStatus.OK;
        var message = "Tickets found";

        return new ResponseEntity<>(PagedApiResult.of(message, myTickets, status), status);
    }
}
