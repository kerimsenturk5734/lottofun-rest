package com.lottofun.lottofunrest.controller;

import com.lottofun.lottofunrest.dto.request.PageableRequest;
import com.lottofun.lottofunrest.dto.wrapper.ApiResult;
import com.lottofun.lottofunrest.dto.wrapper.PagedApiResult;
import com.lottofun.lottofunrest.model.Draw;
import com.lottofun.lottofunrest.service.DrawService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/draws")
public class DrawController {
    private final DrawService drawService;

    public DrawController(DrawService drawService) {
        this.drawService = drawService;
    }

    @GetMapping("/history")
    public ResponseEntity<PagedApiResult<Draw>> history(@ModelAttribute PageableRequest pageableRequest) {
        var pageable = Pageable.ofSize(pageableRequest.getSize()).withPage(pageableRequest.getPage());

        var data = drawService.getDrawHistory(pageable);
        var status = HttpStatus.OK;
        var message = "Draw history";

        return new ResponseEntity<>(PagedApiResult.of(message, data, status), status);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResult<List<Draw>>> history() {
        var data = drawService.getActiveDraws();
        var status = HttpStatus.OK;
        var message = "Active Draws";

        return new ResponseEntity<>(ApiResult.success(message, data, status), status);
    }
}
