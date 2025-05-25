package com.lottofun.lottofunrest.service;

import com.lottofun.lottofunrest.dto.UserDto;
import com.lottofun.lottofunrest.exception.ConflictException;
import com.lottofun.lottofunrest.exception.InsufficientBalanceException;
import com.lottofun.lottofunrest.exception.NotFoundException;
import com.lottofun.lottofunrest.mapper.UserMapper;
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

    public UserDto getUserDtoByUsername(String username) {
        // Find user by username. Throw not found if not exist
        var user = getUserByUsername(username);

        return UserMapper.userAndUserDto().convert(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public User createUser(User user) {
        if (userExistsByUsername(user.getUsername())) {
            throw new ConflictException("User", "Username", user.getUsername());
        }

        return userRepository.save(user);
    }

    public double deposit(String username, double amount) {
        var user = getUserByUsername(username);
        user.setBalance(user.getBalance() + amount);

        return saveUser(user).getBalance();
    }

    public double withdraw(String username, double amount) {
        var user = getUserByUsername(username);

        var newBalance = user.getBalance() - amount;

        if(newBalance < 0)
            throw new InsufficientBalanceException();

        user.setBalance(newBalance);

        return saveUser(user).getBalance();
    }
}
