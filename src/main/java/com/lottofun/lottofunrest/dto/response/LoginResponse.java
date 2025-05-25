package com.lottofun.lottofunrest.dto.response;

import java.time.Instant;

public record LoginResponse(String token, Instant expireAt) {
}
