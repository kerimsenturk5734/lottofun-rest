package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.exception.NotFoundException;
import com.lottofun.lottofunrest.model.User;
import com.lottofun.lottofunrest.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        // Find user by username. Throw not found if not exist
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", "Username", username));
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public User createUser(User user) {
        if (userExistsByUsername(user.getUsername())) {
            throw new RuntimeException("User already exists by username: " + user.getUsername());
        }

        return userRepository.save(user);
    }
}
