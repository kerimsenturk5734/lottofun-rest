package com.lottofun.lottofunrest.controller;

import com.lottofun.lottofunrest.dto.UserDto;
import com.lottofun.lottofunrest.dto.request.LoginRequest;
import com.lottofun.lottofunrest.dto.request.RegisterRequest;
import com.lottofun.lottofunrest.dto.response.LoginResponse;
import com.lottofun.lottofunrest.dto.wrapper.ApiResult;
import com.lottofun.lottofunrest.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User login successful",
                    content = @Content(
                            schema = @Schema(implementation = ApiResult.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "status": 201
                                          "success": success,
                                          "message": "Insufficient balance",
                                          "data": {
                                                "token": "eyJhbGciOiJIUzI1NiI...",
                                                "expireAt": "2021-12-31T23:59:59.999Z",
                                          }
                                        }
                                    """)
                    )
            ),
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResult<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        var status = HttpStatus.CREATED;
        var data = authService.login(loginRequest);
        String message = "User login successful";

        return new ResponseEntity<>(ApiResult.success(message, data, status), status);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(
                            schema = @Schema(implementation = ApiResult.class),
                            examples = @ExampleObject(value = """
                                        {
                                          "status": 201
                                          "success": true,
                                          "message": "Insufficient balance",
                                          "data": {
                                               "id": 1,
                                               "name": "john",
                                               "surname": "doe",
                                               "username": "johndoe",
                                               "balance": 100
                                             }
                                        }
                                    """)
                    )
            ),
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResult<UserDto>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        var status = HttpStatus.CREATED;
        var data = authService.register(registerRequest);
        var message = "User registered successfully";

        return new ResponseEntity<>(ApiResult.success(message, data, status), status);
    }
}
