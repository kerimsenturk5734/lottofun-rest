package com.lottofun.lottofunrest.dto.request;

import org.springframework.web.bind.annotation.RequestParam;

public record PageableRequest(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size) {
}
