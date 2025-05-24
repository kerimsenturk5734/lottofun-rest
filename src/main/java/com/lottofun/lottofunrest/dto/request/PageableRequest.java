package com.lottofun.lottofunrest.dto.request;


import jakarta.annotation.Nullable;

public record PageableRequest(
        @Nullable Integer page,
        @Nullable Integer size
) {
    public int getPage() {
        return page != null && page >= 0 ? page : 0;
    }

    public int getSize() {
        return size != null && size > 0 ? size : 10;
    }
}