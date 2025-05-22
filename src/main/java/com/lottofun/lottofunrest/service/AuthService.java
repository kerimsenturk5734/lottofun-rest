package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.dto.UserDto;
import com.lottofun.lottofunrest.dto.request.LoginRequest;
import com.lottofun.lottofunrest.dto.request.RegisterRequest;
import com.lottofun.lottofunrest.dto.response.LoginResponse;
import com.lottofun.lottofunrest.model.User;
import com.lottofun.lottofunrest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        // Get User by username
        Optional<User> userOpt = userRepository.findByUsername(request.username());

        // throw if user not found
        if (userOpt.isEmpty()) throw new RuntimeException("User not found");

        // get user if presented
        User user = userOpt.get();

        // match passwords
        if (!request.password().equals(user.getPassword())) throw new RuntimeException("Invalid credentials");

        // return response
        return new LoginResponse();
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
                .password(request.password()).build();

        // create user
        User createdUser = userRepository.save(user);

        return new UserDto(createdUser.getId(), createdUser.getName(), createdUser.getSurname(),
                createdUser.getSurname(), createdUser.getBalance());
    }
}
