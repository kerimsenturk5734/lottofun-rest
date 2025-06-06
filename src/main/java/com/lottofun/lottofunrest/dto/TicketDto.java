package com.lottofun.lottofunrest.dto;

import com.lottofun.lottofunrest.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {
    private Long id;
    private Long drawId;
    private Set<Integer> numbers;
    private Instant purchaseTime;
    private TicketStatus status;
    private Integer matchedCount;
    private double prize;
}
