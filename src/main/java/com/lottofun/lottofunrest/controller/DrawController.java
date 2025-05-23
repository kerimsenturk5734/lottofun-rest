package com.lottofun.lottofunrest.controller;

import com.lottofun.lottofunrest.dto.wrapper.ApiResult;
import com.lottofun.lottofunrest.model.Draw;
import com.lottofun.lottofunrest.service.DrawService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/draws")
public class DrawController {
    private final DrawService drawService;

    public DrawController(DrawService drawService) {
        this.drawService = drawService;
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResult<List<Draw>>> history() {
        var data = drawService.getDrawHistory();
        var status = HttpStatus.OK;
        var message = "Draw history";

        return new ResponseEntity<>(ApiResult.success(message, data, status), status);
    }
}
