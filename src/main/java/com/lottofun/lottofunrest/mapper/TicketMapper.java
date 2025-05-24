package com.lottofun.lottofunrest.mapper;

import com.lottofun.lottofunrest.dto.TicketDto;
import com.lottofun.lottofunrest.model.Ticket;
import com.lottofun.lottofunrest.util.mapper.GenericMapper;

public class TicketMapper {
    public static GenericMapper<Ticket, TicketDto> ticketAndTicketDto() {
        return GenericMapper.createMap(
                ticket -> TicketDto.builder()
                        .id(ticket.getId())
                        .drawId(ticket.getDrawId())
                        .numbers(ticket.getNumbers())
                        .purchaseTime(ticket.getPurchaseTime())
                        .status(ticket.getStatus())
                        .matchedCount(ticket.getMatchedCount())
                        .prize(ticket.getPrize())
                        .build(),

                dto -> Ticket.builder()
                        .id(dto.getId())
                        .drawId(dto.getDrawId())
                        .numbers(dto.getNumbers())
                        .purchaseTime(dto.getPurchaseTime())
                        .status(dto.getStatus())
                        .matchedCount(dto.getMatchedCount())
                        .prize(dto.getPrize())
                        .build()
        );
    }
}
