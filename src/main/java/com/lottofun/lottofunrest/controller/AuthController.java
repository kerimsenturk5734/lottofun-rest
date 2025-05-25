package com.lottofun.lottofunrest.controller;

import com.lottofun.lottofunrest.dto.UserDto;
import com.lottofun.lottofunrest.dto.request.LoginRequest;
import com.lottofun.lottofunrest.dto.request.RegisterRequest;
import com.lottofun.lottofunrest.dto.response.LoginResponse;
import com.lottofun.lottofunrest.dto.wrapper.ApiResult;
import com.lottofun.lottofunrest.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResult<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        var status = HttpStatus.CREATED;
        var data = authService.login(loginRequest);
        String message = "User login successful";

        return new ResponseEntity<>(ApiResult.success(message, data, status), status);
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResult<UserDto>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        var status = HttpStatus.CREATED;
        var data = authService.register(registerRequest);
        var message = "User registered successfully";

        return new ResponseEntity<>(ApiResult.success(message, data, status), status);
    }
}
