package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.dto.UserDto;
import com.lottofun.lottofunrest.dto.request.LoginRequest;
import com.lottofun.lottofunrest.dto.request.RegisterRequest;
import com.lottofun.lottofunrest.dto.response.LoginResponse;
import com.lottofun.lottofunrest.model.User;
import com.lottofun.lottofunrest.repository.UserRepository;
import com.lottofun.lottofunrest.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        // Get authentication via authentication manager
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        // generate token if user authenticated
        if (auth.isAuthenticated()){

            // Build response data
            String token = jwtUtil.generateToken(request.username());
            Date expiryAt = jwtUtil.getClaims(token).getExpiration();

            return new LoginResponse(token, expiryAt);
        }

        throw new UsernameNotFoundException("User Id or password incorrect");
    }

    public UserDto register(RegisterRequest request) {

        // Check username existence
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Build new user
        User user = User.builder()
                .name(request.name())
                .surname(request.surname())
                .username(request.username())
                .password(passwordEncoder.encode(request.password())).build();

        // create user
        User createdUser = userRepository.save(user);

        return new UserDto(createdUser.getId(), createdUser.getName(), createdUser.getSurname(),
                createdUser.getUsername(), createdUser.getBalance());
    }
}
