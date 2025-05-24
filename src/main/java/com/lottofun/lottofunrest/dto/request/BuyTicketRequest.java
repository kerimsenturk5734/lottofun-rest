package com.lottofun.lottofunrest.dto.request;

import com.lottofun.lottofunrest.validation.ValidTicketNumber.ValidTicketNumbers;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record BuyTicketRequest(
        long drawId,
        @Size(min = 5, max = 5, message = "Numbers must contain 5 unique numbers")
        @ValidTicketNumbers
        Set<Integer> numbers) {
}
