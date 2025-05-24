package com.lottofun.lottofunrest.controller;

import com.lottofun.lottofunrest.dto.TicketDto;
import com.lottofun.lottofunrest.dto.request.BuyTicketRequest;
import com.lottofun.lottofunrest.dto.request.PageableRequest;
import com.lottofun.lottofunrest.dto.wrapper.ApiResult;
import com.lottofun.lottofunrest.dto.wrapper.PagedApiResult;
import com.lottofun.lottofunrest.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/me")
@Validated
public class MeController {
    private final TicketService ticketService;

    public MeController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public ResponseEntity<PagedApiResult<TicketDto>> getMyTickets(
            @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute PageableRequest pageableRequest) {
        var pageable = Pageable.ofSize(pageableRequest.getSize()).withPage(pageableRequest.getPage());

        var myTickets = ticketService.getTicketsByUser_Username(userDetails.getUsername(), pageable);

        var status = HttpStatus.OK;
        var message = "Tickets found";

        return new ResponseEntity<>(PagedApiResult.of(message, myTickets, status), status);
    }

    @PostMapping("/tickets/buy")
    public ResponseEntity<ApiResult<TicketDto>> buyTicket(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody BuyTicketRequest buyTicketRequest) {

        var boughtTicket = ticketService.buyTicket(userDetails.getUsername(), buyTicketRequest.drawId(), buyTicketRequest.numbers());

        var status = HttpStatus.CREATED;
        var message = "Ticket bought for draw:" + buyTicketRequest.drawId() + " successfully";

        return new ResponseEntity<>(ApiResult.success(message, boughtTicket, status), status);
    }
}
