package com.lottofun.lottofunrest.dto.response;

import java.util.Date;

public record LoginResponse(String token, Date expireAt) {
}
