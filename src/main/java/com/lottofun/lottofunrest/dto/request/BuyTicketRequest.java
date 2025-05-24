package com.lottofun.lottofunrest.dto.request;

import java.util.Set;

public record BuyTicketRequest(long drawId, Set<Integer> numbers) {
}
