package com.lottofun.lottofunrest.dto.request;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PageableRequest(

        @Nullable
        @Min(value = 0, message = "Page must be 0 or greater")
        Integer page,

        @Nullable
        @Min(value = 1, message = "Page Size must be at least 1")
        @Max(value = 100, message = "Page Size must not be greater than 100")
        Integer size

) {
    public int getPage() {
        return page != null && page >= 0 ? page : 0;
    }

    public int getSize() {
        return size != null && size > 0 ? size : 10;
    }
}